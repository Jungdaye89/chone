package com.chone.server.domains.user.dto.request;

import com.chone.server.domains.user.domain.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleUpdateRequestDto {
  private Role role;
}
