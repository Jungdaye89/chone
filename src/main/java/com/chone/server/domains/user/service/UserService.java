package com.chone.server.domains.user.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.ProductFacade;
import com.chone.server.commons.facade.StoreFacade;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.product.service.ProductService;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.service.StoreService;
import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import com.chone.server.domains.user.dto.request.SignupRequestDto;
import com.chone.server.domains.user.dto.request.UserRoleUpdateRequestDto;
import com.chone.server.domains.user.dto.request.UserUpdateRequestDto;
import com.chone.server.domains.user.dto.response.SearchUserResponseDto;
import com.chone.server.domains.user.dto.response.UserResponseDto;
import com.chone.server.domains.user.exception.UserExceptionCode;
import com.chone.server.domains.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  private final StoreService storeService;
  private final StoreFacade storeFacade;
  private final ProductService productService;
  private final ProductFacade productFacade;

  //회원가입
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

    User user = User.builder(username,
        email,
        bCryptPasswordEncoder.encode(password),
        role != null ? role : Role.CUSTOMER, // 요청에 role이 없으면 기본값 CUSTOMER,
        true
    ).build();

    userRepository.save(user);
  }

  //회원목록 조회
  public Page<SearchUserResponseDto> findUsers(CustomUserDetails currentUser, String username,
      String email, String role,
      LocalDate startDate, LocalDate endDate, Pageable pageable) {
    //현재 사용자가 MASTER, MANAGER인지 확인
    if (!(currentUser.getRole() == Role.MASTER || currentUser.getRole() == Role.MANAGER)) {
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }
    Page<User> findUsers = userRepository.findUsers(username, email, role, startDate, endDate,
        pageable);

    return findUsers.map(SearchUserResponseDto::fromEntity);
  }

  //특정 회원정보 조회
  public UserResponseDto findUserByUserId(Long userId) {

    return UserResponseDto.fromEntity(userRepository.findByIdAndDeletedAtIsNull(userId)
        .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND)));
  }

  //회원 정보 수정
  @Transactional
  public UserResponseDto updateUser(Long id, UserUpdateRequestDto requestDto,
      CustomUserDetails currentUser) {

    User user = userRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    //조회한 아이디와 현재 아이디가 다른 경우에는 수정불가
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

  //Master나 Manager가 회원의 권한을 Owner로 변경
  @Transactional
  public UserResponseDto updateUserRole(Long id, UserRoleUpdateRequestDto userRoleUpdateRequestDto,
      CustomUserDetails currentUser) {

    User user = userRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    // 현재 사용자의 역할이 MANAGER 또는 MASTER인지 체크
    if (!(currentUser.getRole() == Role.MANAGER || currentUser.getRole() == Role.MASTER)) {
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }

    user.updateRole(userRoleUpdateRequestDto.getRole());
    return UserResponseDto.fromEntity(user);
  }

  //휴면 계정 전환
  @Transactional
  public UserResponseDto updateStatus(Long id, CustomUserDetails currentUser) {

    User user = getUserWithAuthorityCheck(id, currentUser);
    user.updateIsAvailable();
    userRepository.save(user);

    return UserResponseDto.fromEntity(user);
  }

  @Transactional
  public void deleteUser(Long id, CustomUserDetails currentUser) {

    User user = getUserWithAuthorityCheck(id, currentUser);
    user.updateIsAvailable();
    user.delete(user);

    //해당 유저의 가게 찾기
    List<Store> allStoreByUserId = storeFacade.findAllStoreByUserId(user.getId());
    //가게 삭제하기
    allStoreByUserId.forEach(
        store -> storeFacade.deleteStore(currentUser.getUser(), store));

    //주문삭제

  }

  private User getUserWithAuthorityCheck(Long id, CustomUserDetails currentUser) {

    User user = userRepository.findByIdAndDeletedAtIsNull(id)
        .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    //권한 체크(본인이거나, MANAGER 또는 MASTER 권한이어야 함)
    if (!(user.getUsername().equals(currentUser.getUsername()) ||
        currentUser.getRole().equals(Role.MANAGER) ||
        currentUser.getRole().equals(Role.MASTER))) {
      throw new ApiBusinessException(UserExceptionCode.USER_NOT_AUTHORITY);
    }
    return user;
  }
}