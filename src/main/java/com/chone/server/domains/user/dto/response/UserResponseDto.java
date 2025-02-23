package com.chone.server.domains.user.dto.response;

import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "유저 조회 응답 DTO")
public class UserResponseDto {
  @Schema(description = "유저 고유번호", example = "1")
  private Long id;
  @Schema(description = "유저 아이디", example = "customer1")
  private String username;
  @Schema(description = "유저 이메일", example = "customer1@naver.com")
  private String email;
  @Schema(description = "유저 권한", example = "CUSTOMER")
  private Role role;
  @Schema(description = "유저 활성화 비활성화 여부", example = "true")
  private boolean isAvailable;

  public static UserResponseDto fromEntity(User user) {
    return new UserResponseDto(
        user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getIsAvailable());
  }
}
