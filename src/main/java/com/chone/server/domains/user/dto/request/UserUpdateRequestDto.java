package com.chone.server.domains.user.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequestDto {
  private String password;

  @Email(message = "올바른 형식의 이메일 주소를 입력해주세요.")
  private String email;
}
