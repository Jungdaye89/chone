package com.chone.server.domains.user.dto.request;

import com.chone.server.domains.user.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원가입 요청Dto")
public class SignupRequestDto {

  @NotBlank(message = "사용자명을 입력해주세요.")
  @Size(min = 4, max = 10, message = "사용자명은 4~10자여야 합니다.")
  @Pattern(regexp = "^[a-z0-9]+$", message = "사용자명은 소문자와 숫자로만 이루어져야 합니다.")
  @Schema(description = "사용자 아이디", example = "customer1")
  private String username;

  @NotBlank(message = "비밀번호를 입력해주세요.")
  @Size(min = 8, max = 15, message = "비밀번호는 8~15자여야 합니다.")
  @Pattern(
      regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-={}|;:'\",.<>?/]).{8,15}$",
      message = "비밀번호는 대소문자, 숫자, 특수문자를 포함해야 합니다.")
  @Schema(description = "사용자 비밀번호", example = "Password123!")
  private String password;

  @NotBlank(message = "이메일 주소를 입력해주세요.")
  @Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
  @Schema(description = "사용자 이메일", example = "customer1@naver.com")
  private String email;

  @Schema(description = "사용자 권한", example = "CUSTOMER")
  private Role role;
}
