package com.chone.server.domains.user.dto.response;

import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReadResponseDto {
  private Long id;
  private String username;
  private String email;
  private Role role;
  private LocalDateTime createdAt;

  public static ReadResponseDto fromEntity(User user) {
    return ReadResponseDto.builder()
        .id(user.getId())
        .username(user.getUsername())
        .email(user.getEmail())
        .role(user.getRole())
        .createdAt(user.getCreatedAt())
        .build();
  }
}
