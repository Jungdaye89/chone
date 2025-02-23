package com.chone.server.domains.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "로그인 응답Dto")
public class LoginResponseDto {
  @Schema(description = "사용자 아이디", example = "customer1")
  private String username;

  @Schema(
      description = "로그인 한 JWT",
      example =
          "\"Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjdXN0b21lcjEyIiwiYXV0aCI6IkNVU1RPTUVSIiwiZXhwIjoxNzQwMzAyMzI2LCJpYXQiOjE3NDAzMDIzMjZ9.pToSP1ZgiDn3QVd-3zKpCOj9sNjwGKIMTjfx4XMABUI")
  private String token;
}
