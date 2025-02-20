package com.chone.server.domains.user.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.user.dto.request.SignupRequestDto;
import com.chone.server.domains.user.dto.request.UserRoleUpdateRequestDto;
import com.chone.server.domains.user.dto.request.UserUpdateRequestDto;
import com.chone.server.domains.user.dto.response.UserResponseDto;
import com.chone.server.domains.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        userService.signUp(signupRequestDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('MASTER')")
    public ResponseEntity<List<UserResponseDto>> getUsers(@AuthenticationPrincipal CustomUserDetails currentUser){
        List<UserResponseDto> userResponseDtoList = userService.findUserList(currentUser);
        return ResponseEntity.ok().body(userResponseDtoList);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable Long userId) {
        UserResponseDto userResponseDto = userService.findUserByUserId(userId);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable("userId") Long userId,
                                        @RequestBody UserUpdateRequestDto userUpdateRequestDto,
                                        @AuthenticationPrincipal CustomUserDetails currentUser) {
        UserResponseDto userResponseDto = userService.updateUser(userId, userUpdateRequestDto, currentUser);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @PatchMapping("/{userId}/role")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('MASTER')")
    public ResponseEntity<UserResponseDto> updateUserRole(@PathVariable("userId") Long userId,
                                            @RequestBody UserRoleUpdateRequestDto userRoleUpdateRequestDto,
                                            @AuthenticationPrincipal CustomUserDetails currentUser){
        UserResponseDto userResponseDto = userService.updateUserRole(userId, userRoleUpdateRequestDto, currentUser);
        return ResponseEntity.ok().body(userResponseDto);
    }

    @PatchMapping("/{userId}/status")
    public ResponseEntity<UserResponseDto> updateUserStatus(
            @PathVariable("userId") Long userId,
            @AuthenticationPrincipal CustomUserDetails currentUser){
        UserResponseDto updatedUserDto = userService.updateStatus(userId, currentUser);
        return ResponseEntity.ok().body(updatedUserDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable("userId") Long userId,
                                        @AuthenticationPrincipal CustomUserDetails currentUser) {
        userService.deleteUser(userId, currentUser);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
