package com.chone.server.domains.user.dto.response;

import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDto {
  private Long id;
  private String username;
  private String email;
  private Role role;
  private boolean isAvailable;

  public static UserResponseDto fromEntity(User user) {
    return new UserResponseDto(
        user.getId(), user.getUsername(), user.getEmail(), user.getRole(), user.getIsAvailable());
  }
}
