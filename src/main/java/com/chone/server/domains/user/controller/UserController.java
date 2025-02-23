package com.chone.server.domains.user.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.user.base.BaseResponseBody;
import com.chone.server.domains.user.base.DataResponseBody;
import com.chone.server.domains.user.dto.request.ActivateUserRequestDto;
import com.chone.server.domains.user.dto.request.SignupRequestDto;
import com.chone.server.domains.user.dto.request.UserRoleUpdateRequestDto;
import com.chone.server.domains.user.dto.request.UserUpdateRequestDto;
import com.chone.server.domains.user.dto.response.SearchUserResponseDto;
import com.chone.server.domains.user.dto.response.UserResponseDto;
import com.chone.server.domains.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Tag(name = "1. 회원", description = "유저 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

  private final UserService userService;

  @Operation(summary = "회원가입 API", description = "\n\n 사용자는 회원가입을 위해 아이디, 비밀번호, 이메일을 필수로 입력한다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "ALL",
            description = "성공 \n\n Success반환",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BaseResponseBody.class),
                    examples = {
                      @ExampleObject(
                          name = "회원가입 성공",
                          description = "사용자는 회원가입 성공시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                    {
                        "status": 201,
                        "message": "회원가입성공",
                        "code": "SUCCESS"
                    }
                    """),
                      @ExampleObject(
                          name = "회원가입 실패",
                          description = "사용자는 회원가입 실패시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                    {
                        "status": 403,
                        "message": "회원가입실패",
                        "code": "FAIL"
                    }
                    """)
                    }))
      })
  @PostMapping("/signup")
  public ResponseEntity<? extends BaseResponseBody> signup(
      @Valid @RequestBody SignupRequestDto signupRequestDto, BindingResult result) {
    if (result.hasErrors()) {
      ResponseEntity.status(HttpStatus.BAD_REQUEST)
          .body(BaseResponseBody.of(403, "회원가입실패", "FAIL"));
    }
    userService.signUp(signupRequestDto);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponseBody.of(201, "회원가입성공", "SUCCESS"));
  }

  @Operation(summary = "유저조회 API", description = "\n\n MASTER,MANAGER만 사용자목록을 조회한다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "ALL",
            description = "성공 \n\n Success반환",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataResponseBody.class),
                    examples = {
                      @ExampleObject(
                          name = "회원목록 조회 성공",
                          description = "사용자는 회원목록 조회 성공시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                                            {
                                                "status": 200,
                                                "message": "회원목록 조회 성공",
                                                "code": "SUCCESS",
                                                "data": {
                                                    "content": [
                                                        {
                                                            "id": 7,
                                                            "username": "customer7",
                                                            "email": "testEmail1@naver.com",
                                                            "role": "CUSTOMER",
                                                            "createdAt": "2025-02-22T02:21:45.090196"
                                                        },
                                                        {
                                                            "id": 6,
                                                            "username": "customer10",
                                                            "email": "testtest2@naver.com",
                                                            "role": "CUSTOMER",
                                                            "createdAt": "2025-02-20T12:22:19.163278"
                                                        }
                                                    ],
                                                    "pageInfo": {
                                                        "page": 0,
                                                        "size": 10,
                                                        "totalElements": 5,
                                                        "totalPages": 1
                                                    }
                                                }
                                            }
                                """),
                      @ExampleObject(
                          name = "회원목록 조회 실패",
                          description = "사용자는 회원목록 조회 실패시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                                            {
                                                "status": 403,
                                                "message": "회원목록 조회 실패",
                                                "code": "FAIL"
                                            }
                                            """)
                    }))
      })
  @GetMapping
  @PreAuthorize("hasAnyRole('MANAGER','MASTER')")
  public ResponseEntity<? extends DataResponseBody> getUsers(
      @AuthenticationPrincipal CustomUserDetails currentUser,
      @RequestParam(required = false) String username,
      @RequestParam(required = false) String email,
      @RequestParam(required = false) String role,
      @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate,
      @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC)
          Pageable pageable) {
    SearchUserResponseDto searchUserResponseDtoPage =
        userService.findUsers(currentUser, username, email, role, startDate, endDate, pageable);
    return ResponseEntity.status(HttpStatus.OK)
        .body(DataResponseBody.of(200, "회원목록 조회 성공", "SUCCESS", searchUserResponseDtoPage));
  }

  @Operation(summary = "유저상세조회 API", description = "\n\n 유저 상세정보를 조회한다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "ALL",
            description = "성공 \n\n Success반환",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataResponseBody.class),
                    examples = {
                      @ExampleObject(
                          name = "유저 상세조회 성공",
                          description = "사용자는 유저 상세조회 성공시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                        {
                            "status": 201,
                            "message": "유저 상세조회 성공",
                            "code": "SUCCESS",
                            "data":
                                [
                                    {
                                        "id": 1,
                                        "username": "john_doe",
                                        "email": "john.doe@example.com",
                                        "role": "USER",
                                        "isAvailable": true
                                    }
                                ]
                        }
                        """),
                      @ExampleObject(
                          name = "유저 상세조회 실패",
                          description = "사용자는 유저 상세조회 실패시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                        {
                            "status": 403,
                            "message": "유저 상세조회 실패",
                            "code": "FAIL"
                        }
                        """)
                    }))
      })
  @GetMapping("/{userId}")
  public ResponseEntity<? extends DataResponseBody> getUser(
      @PathVariable Long userId,
      @AuthenticationPrincipal CustomUserDetails currentUser) {
    UserResponseDto userResponseDto = userService.findUserByUserId(userId, currentUser);
    return ResponseEntity.status(HttpStatus.OK)
        .body(DataResponseBody.of(200, "유저 상세조회 성공", "SUCCESS", userResponseDto));
  }

  @Operation(summary = "비밀번호 및 이메일 변경 API", description = "\n\n 비밀번호 및 이메일 변경한다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "ALL",
            description = "성공 \n\n Success반환",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataResponseBody.class),
                    examples = {
                      @ExampleObject(
                          name = "비밀번호 및 이메일 변경 성공",
                          description = "사용자는 비밀번호 및 이메일 변경 성공시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                        {
                            "status": 201,
                            "message": "비밀번호 및 이메일 변경 성공",
                            "code": "SUCCESS",
                            "data":
                                [
                                    {
                                        "id": 1,
                                        "username": "john_doe",
                                        "email": "updatedjohn.doe@example.com",
                                        "role": "USER",
                                        "isAvailable": true
                                    }
                                ]
                        }
                        """),
                      @ExampleObject(
                          name = "비밀번호 및 이메일 변경 실패",
                          description = "사용자는 비밀번호 및 이메일 변경 실패시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                        {
                            "status": 403,
                            "message": "비밀번호 및 이메일 변경 실패",
                            "code": "FAIL"
                        }
                        """)
                    }))
      })
  @PutMapping("/{userId}")
  public ResponseEntity<? extends DataResponseBody> updateUser(
      @PathVariable("userId") Long userId,
      @RequestBody UserUpdateRequestDto userUpdateRequestDto,
      @AuthenticationPrincipal CustomUserDetails currentUser) {
    UserResponseDto userResponseDto =
        userService.updateUser(userId, userUpdateRequestDto, currentUser);
    return ResponseEntity.status(HttpStatus.OK)
        .body(DataResponseBody.of(200, "비밀번호 및 이메일 변경 성공", "SUCCESS", userResponseDto));
  }

  @Operation(summary = "유저 권한 변경 API", description = "\n\n MASTER,MANAGER만 유저 권한을 변경한다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "ALL",
            description = "성공 \n\n Success반환",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataResponseBody.class),
                    examples = {
                      @ExampleObject(
                          name = "유저 권한 변경 성공",
                          description = "사용자는 유저 권한 변경 성공시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                        {
                            "status": 201,
                            "message": "유저 권한 변경 성공",
                            "code": "SUCCESS",
                            "data":
                                [
                                    {
                                        "id": 1,
                                        "username": "john_doe",
                                        "email": "updatedjohn.doe@example.com",
                                        "role": "OWNER",
                                        "isAvailable": true
                                    }
                                ]
                        }
                        """),
                      @ExampleObject(
                          name = "회원목록 조회 실패",
                          description = "사용자는 회원목록 조회 실패시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                        {
                            "status": 403,
                            "message": "유저 권한 변경 실패",
                            "code": "FAIL"
                        }
                        """)
                    }))
      })
  @PatchMapping("/{userId}/role")
  @PreAuthorize("hasAnyRole('MANAGER','MASTER')")
  public ResponseEntity<?> updateUserRole(
      @PathVariable("userId") Long userId,
      @RequestBody UserRoleUpdateRequestDto userRoleUpdateRequestDto,
      @AuthenticationPrincipal CustomUserDetails currentUser) {
    UserResponseDto userResponseDto =
        userService.updateUserRole(userId, userRoleUpdateRequestDto, currentUser);
    return ResponseEntity.status(HttpStatus.OK)
        .body(DataResponseBody.of(200, "유저 권한 변경 성공", "SUCCESS", userResponseDto));
  }

  @Operation(summary = "계정 비활성화 API", description = "\n\n 사용자는 계정 비활성화를 한다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "ALL",
            description = "성공 \n\n Success반환",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataResponseBody.class),
                    examples = {
                      @ExampleObject(
                          name = "계정 비활성화",
                          description = "사용자는 계정 비활성화 성공시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                        {
                            "status": 201,
                            "message": "계정 비활성화",
                            "code": "SUCCESS",
                            "data":
                                [
                                    {
                                        "id": 1,
                                        "username": "john_doe",
                                        "email": "updatedjohn.doe@example.com",
                                        "role": "USER",
                                        "isAvailable": false
                                    }
                                ]
                        }
                        """),
                      @ExampleObject(
                          name = "계정 비활성화 실패",
                          description = "사용자는 계정 비활성화 실패시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                        {
                            "status": 403,
                            "message": "계정 비활성화 실패",
                            "code": "FAIL"
                        }
                        """)
                    }))
      })
  @PatchMapping("/{userId}/deactive")
  public ResponseEntity<? extends DataResponseBody> deactiveUser(
      @PathVariable("userId") Long userId, @AuthenticationPrincipal CustomUserDetails currentUser) {
    UserResponseDto updatedUserDto = userService.deactivateUser(userId, currentUser);
    return ResponseEntity.status(HttpStatus.OK)
        .body(DataResponseBody.of(200, "계정 비활성화", "SUCCESS", updatedUserDto));
  }

  @Operation(summary = "계정 활성화 API", description = "\n\n 사용자는 계정 활성화를 한다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "ALL",
            description = "성공 \n\n Success반환",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = DataResponseBody.class),
                    examples = {
                      @ExampleObject(
                          name = "계정 활성화",
                          description = "사용자는 계정 활성화 성공시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                        {
                            "status": 201,
                            "message": "계정 활성화",
                            "code": "SUCCESS",
                            "data":
                                [
                                    {
                                        "id": 1,
                                        "username": "john_doe",
                                        "email": "updatedjohn.doe@example.com",
                                        "role": "USER",
                                        "isAvailable": ture
                                    }
                                ]
                        }
                        """),
                      @ExampleObject(
                          name = "계정 활성화 실패",
                          description = "사용자는 계정 활성화 실패시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                        {
                            "status": 403,
                            "message": "계정 활성화 실패",
                            "code": "FAIL"
                        }
                        """)
                    }))
      })
  @PatchMapping("/activate")
  public ResponseEntity<? extends DataResponseBody> activateUser(
      @RequestBody ActivateUserRequestDto request) {
    UserResponseDto updatedUserDto =
        userService.activateUser(request.getUsername(), request.getPassword());
    return ResponseEntity.status(HttpStatus.OK)
        .body(DataResponseBody.of(200, "계정 활성화", "SUCCESS", updatedUserDto));
  }

  @Operation(summary = "회원탈퇴 API", description = "\n\n 사용자는 회원탈퇴를 한다.")
  @ApiResponses(
      value = {
        @ApiResponse(
            responseCode = "ALL",
            description = "성공 \n\n Success반환",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BaseResponseBody.class),
                    examples = {
                      @ExampleObject(
                          name = "회원탈퇴 성공",
                          description = "사용자는 회원탈퇴 성공시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                    {
                        "status": 201,
                        "message": "회원탈퇴성공",
                        "code": "SUCCESS"
                    }
                    """),
                      @ExampleObject(
                          name = "회원탈퇴 실패",
                          description = "사용자는 회원탈퇴 실패시 다음과 같은 응답데이터를 받는다.",
                          value =
                              """
                    {
                        "status": 403,
                        "message": "회원탈퇴실패",
                        "code": "FAIL"
                    }
                    """)
                    }))
      })
  @DeleteMapping("/{userId}")
  public ResponseEntity<? extends BaseResponseBody> deleteUser(
      @PathVariable("userId") Long userId, @AuthenticationPrincipal CustomUserDetails currentUser) {
    userService.deleteUser(userId, currentUser);
    return ResponseEntity.status(HttpStatus.OK)
        .body(BaseResponseBody.of(201, "회원탈퇴성공", "SUCCESS"));
  }
}
