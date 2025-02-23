package com.chone.server.domains.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "로그인 요청Dto")
public class LoginRequestDto {
  @Schema(description = "사용자 아이디", example = "customer1")
  private String username;
  @Schema(description = "사용자 비밀번호", example = "Password123!")
  private String password;
}
