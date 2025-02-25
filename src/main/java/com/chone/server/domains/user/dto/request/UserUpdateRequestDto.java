package com.chone.server.domains.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "회원정보변경 요청Dto")
public class UserUpdateRequestDto {
  @Schema(description = "사용자 변경 비밀번호", example = "newPassword123!")
  private String password;

  @Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
  @Schema(description = "사용자 변경 이메일", example = "newcustomer1@naver.com")
  private String email;
}
