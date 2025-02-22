package com.chone.server.domains.user.dto.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDto {
  private String username;
  private String password;
}
