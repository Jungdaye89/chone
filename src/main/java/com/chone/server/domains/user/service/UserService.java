package com.chone.server.domains.user.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.OrderFacade;
import com.chone.server.commons.facade.ReviewFacade;
import com.chone.server.commons.facade.StoreFacade;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.review.domain.Review;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import com.chone.server.domains.user.dto.request.SignupRequestDto;
import com.chone.server.domains.user.dto.request.UserRoleUpdateRequestDto;
import com.chone.server.domains.user.dto.request.UserUpdateRequestDto;
import com.chone.server.domains.user.dto.response.PageInfoDto;
import com.chone.server.domains.user.dto.response.ReadResponseDto;
import com.chone.server.domains.user.dto.response.SearchUserResponseDto;
import com.chone.server.domains.user.dto.response.UserResponseDto;
import com.chone.server.domains.user.exception.UserExceptionCode;
import com.chone.server.domains.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.Authenticator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final StoreFacade storeFacade;
  private final ReviewFacade reviewFacade;
  private final OrderFacade orderFacade;
  private final AuthenticationManager authenticationManager;

  // 회원가입
  public void signUp(SignupRequestDto signupRequestDto) {

    String username = signupRequestDto.getUsername();
    String password = signupRequestDto.getPassword();
    Role role = signupRequestDto.getRole(); // 요청에서 role 값 받기

    // 회원 중복 확인
    Optional<User> checkUsername = userRepository.findByUsername(username);
    if (checkUsername.isPresent()) {
      throw new ApiBusinessException(UserExceptionCode.USER_EXIST_USERNAME);
    }

    // email 중복확인
    String email = signupRequestDto.getEmail();
    Optional<User> checkEmail = userRepository.findByEmail(email);
    if (checkEmail.isPresent()) {
      throw new ApiBusinessException(UserExceptionCode.USER_EXIST_EMAIL);
    }

    User user =
        User.builder(
                username,
                email,
                bCryptPasswordEncoder.encode(password),
                role != null ? role : Role.CUSTOMER, // 요청에 role이 없으면 기본값 CUSTOMER,
                true)
            .build();

    userRepository.save(user);
  }

  // 회원목록 조회
  public SearchUserResponseDto findUsers(
      CustomUserDetails currentUser,
      String username,
      String email,
      String role,
      LocalDate startDate,
      LocalDate endDate,
      Pageable pageable) {
    // 현재 사용자가 MASTER, MANAGER인지 확인
    if (!(currentUser.getRole() == Role.MASTER || currentUser.getRole() == Role.MANAGER)) {
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }
    Page<User> findUsers =
        userRepository.findUsers(username, email, role, startDate, endDate, pageable);

    return SearchUserResponseDto.builder()
        .content(
            findUsers.getContent().stream()
                .map(ReadResponseDto::fromEntity)
                .collect(Collectors.toList()))
        .pageInfo(
            PageInfoDto.builder()
                .page(findUsers.getNumber())
                .size(findUsers.getSize())
                .totalElements(findUsers.getTotalElements())
                .totalPages(findUsers.getTotalPages())
                .build())
        .build();
  }

  // 특정 회원정보 조회
  public UserResponseDto findUserByUserId(Long userId, CustomUserDetails currentUser) {
    getUserWithAuthorityCheck(userId, currentUser);
    return UserResponseDto.fromEntity(
        userRepository
            .findByIdAndDeletedAtIsNull(userId)
            .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND)));
  }

  // 회원 정보 수정
  @Transactional
  public UserResponseDto updateUser(
      Long id, UserUpdateRequestDto requestDto, CustomUserDetails currentUser) {

    User user =
        userRepository
            .findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    // 조회한 아이디와 현재 아이디가 다른 경우에는 수정불가
    if (!user.getUsername().equals(currentUser.getUsername())) {
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }

    // 비밀번호 변경 로직
    String encodedPassword =
        (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty())
            ? bCryptPasswordEncoder.encode(requestDto.getPassword())
            : null;

    user.updateUser(requestDto.getEmail(), encodedPassword);

    return UserResponseDto.fromEntity(user);
  }

  // Master나 Manager가 회원의 권한을 Owner로 변경
  @Transactional
  public UserResponseDto updateUserRole(
      Long id, UserRoleUpdateRequestDto userRoleUpdateRequestDto, CustomUserDetails currentUser) {

    User user =
        userRepository
            .findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    // 현재 사용자의 역할이 MANAGER 또는 MASTER인지 체크
    if (!(currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.MASTER)) {
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }

    user.updateRole(userRoleUpdateRequestDto.getRole());
    return UserResponseDto.fromEntity(user);
  }

  // 계정 비활성화
  @Transactional
  public UserResponseDto deactivateUser(Long id, CustomUserDetails currentUser) {

    User user = getUserWithAuthorityCheck(id, currentUser);
    if(!user.getIsAvailable()){
      throw new ApiBusinessException(UserExceptionCode.USER_ALREADY_DEACTIVE);
    }
    user.updateIsAvailable();
    userRepository.save(user);

    return UserResponseDto.fromEntity(user);
  }

  // 계정 활성화
  @Transactional
  public UserResponseDto activateUser(String username, String password) {
    // 아이디 & 비밀번호 인증 (SecurityContext 사용 X)
    authenticateUser(username, password);

    // username으로 사용자 조회 (SecurityContext 사용 X)
    User user = userRepository.findByUsernameAndDeletedAtIsNull(username)
        .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    // 이미 활성화된 경우 예외 발생
    if (Boolean.TRUE.equals(user.getIsAvailable())) {
      throw new ApiBusinessException(UserExceptionCode.USER_ALREADY_ACTIVE);
    }

    // 계정 활성화
    user.updateIsAvailable();
    userRepository.save(user);

    return UserResponseDto.fromEntity(user);
  }

  @Transactional
  public void deleteUser(Long id, CustomUserDetails currentUser) {

    User user = getUserWithAuthorityCheck(id, currentUser);
    //IsAvailable이 true일때 false로 변경(즉 이미 비활성화가 되어 있으면 실행 안함)
    if(user.getIsAvailable()){
      user.updateIsAvailable();
    }
    user.delete(user);

    // 해당 유저의 가게 찾기
    List<Store> allStoreByUserId = storeFacade.findAllStoreByUserId(user.getId());
    // 가게 삭제하기
    allStoreByUserId.forEach(store -> storeFacade.deleteStore(currentUser.getUser(), store));

    // 리뷰삭제
    List<Review> allReviewByUserId = reviewFacade.findAllReviews(user.getId());
    allReviewByUserId.forEach(review -> reviewFacade.deleteReview(user, review));

    // 주문삭제
    List<Order> userOrders = orderFacade.findAllOrderByUserId(user.getId());
    userOrders.forEach(order -> orderFacade.deleteOrder(user, order));
  }

  private User getUserWithAuthorityCheck(Long id, CustomUserDetails currentUser) {

    User user =
        userRepository
            .findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    // 권한 체크(본인이거나, MANAGER 또는 MASTER 권한이어야 함)
    if (!(user.getUsername().equals(currentUser.getUsername())
        || currentUser.getRole().equals(Role.MANAGER)
        || currentUser.getRole().equals(Role.MASTER))) {
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }
    return user;
  }


  // 아이디 & 비밀번호 인증 메서드
  private void authenticateUser(String username, String password) {
    UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(username, password);

    try {
      // Spring Security의 인증 매니저를 통해 사용자 인증 시도
      authenticationManager.authenticate(authenticationToken);
    } catch (BadCredentialsException e) {
      // 비밀번호가 일치하지 않는 경우 발생하는 예외 → 사용자가 잘못된 비밀번호를 입력한 경우
      throw new ApiBusinessException(UserExceptionCode.INVALID_CREDENTIALS);
    } catch (DisabledException e) {
      // 계정이 비활성화된 경우라도, 활성화 API이므로 예외를 던지지 않고 통과시킴
      log.warn("비활성화된 계정 로그인 시도: {}", username);
    }
  }
}
