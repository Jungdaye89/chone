package com.chone.server.domains.user.dto.request;

import com.chone.server.domains.user.domain.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 권한 변경 요청Dto")
public class UserRoleUpdateRequestDto {
  @Schema(description = "사용자 권한", example = "OWNER")
  private Role role;
}
