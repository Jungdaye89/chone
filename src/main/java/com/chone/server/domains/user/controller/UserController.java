package com.chone.server.domains.user.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.user.dto.request.SignupRequestDto;
import com.chone.server.domains.user.dto.request.UserRoleUpdateRequestDto;
import com.chone.server.domains.user.dto.request.UserUpdateRequestDto;
import com.chone.server.domains.user.dto.response.UserResponseDto;
import com.chone.server.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    
    @PostMapping("/signup")
    public void signup(@RequestBody SignupRequestDto signupRequestDto) {
        userService.signUp(signupRequestDto);
    }

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
