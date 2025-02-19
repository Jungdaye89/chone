package com.chone.server.domains.user.controller;

import com.chone.server.commons.jwt.JwtUtil;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.user.dto.request.LoginRequestDto;
import com.chone.server.domains.user.dto.request.SignupRequestDto;
import com.chone.server.domains.user.dto.request.UserRoleUpdateRequestDto;
import com.chone.server.domains.user.dto.request.UserUpdateRequestDto;
import com.chone.server.domains.user.dto.response.LoginResponseDto;
import com.chone.server.domains.user.dto.response.UserResponseDto;
import com.chone.server.domains.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService,JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signUp(signupRequestDto);
    }

//    @PostMapping("/login")
//    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto requestDto) {
////        // 사용자 인증 요청
////        Authentication authentication = authenticationManager.authenticate(
////                new UsernamePasswordAuthenticationToken(requestDto.getUsername(), requestDto.getPassword())
////        );
////
////        // 인증 정보를 SecurityContext에 저장
////        SecurityContextHolder.getContext().setAuthentication(authentication);
////
////        // 사용자 정보 가져오기
////        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
////        String role = userDetails.getAuthorities().iterator().next().getAuthority();
////
////        // JWT 토큰 생성
////        String jwt = jwtUtil.createToken(userDetails.getUsername(), role, 60 * 60 * 10L); // 10시간
////
////        LoginResponseDto responseDto = new LoginResponseDto(userDetails.getUsername(), jwt);
//
////        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
//        return ResponseEntity.s;
//    }

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('MASTER')")
    public ResponseEntity<?> getUsers(@AuthenticationPrincipal CustomUserDetails currentUser) throws AccessDeniedException {
        List<UserResponseDto> users = userService.findUserList(currentUser);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUser(@PathVariable Long userId) {
        UserResponseDto user = userService.findUserByUserId(userId);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId,
                                        @RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                        @AuthenticationPrincipal CustomUserDetails currentUser)
                                        throws AccessDeniedException {
        userService.updateUser(userId, userUpdateRequestDto, currentUser);
        return ResponseEntity.ok(userUpdateRequestDto);
    }

    @PatchMapping("/{userId}/role")
    public ResponseEntity<?> updateUserRole(@PathVariable("userId") Long userId,
                                            @RequestBody UserRoleUpdateRequestDto userRoleUpdateRequestDto,
                                            @AuthenticationPrincipal CustomUserDetails currentUser) throws AccessDeniedException {
        userService.updateUserRole(userId, userRoleUpdateRequestDto, currentUser);
        return ResponseEntity.ok(userRoleUpdateRequestDto);
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUser) throws AccessDeniedException {
        UserResponseDto updatedUser = userService.updateStatus(userId, currentUser);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId,
                                        @AuthenticationPrincipal CustomUserDetails currentUser) throws AccessDeniedException {
        userService.deleteUser(userId, currentUser.getUser());
        return ResponseEntity.ok().build();
    }

}
