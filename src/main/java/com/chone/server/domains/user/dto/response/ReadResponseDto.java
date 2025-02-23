package com.chone.server.domains.user.dto.response;

import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "유저 조회 응답 DTO")
public class ReadResponseDto {
  @Schema(description = "유저 고유번호", example = "1")
  private Long id;
  @Schema(description = "유저 아이디", example = "customer1")
  private String username;
  @Schema(description = "유저 이메일", example = "customer1@naver.com")
  private String email;
  @Schema(description = "유저 권한", example = "CUSTOMER")
  private Role role;
  @Schema(description = "유저 생성일", example = "2025-02-20 01:48:02.257833")
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
