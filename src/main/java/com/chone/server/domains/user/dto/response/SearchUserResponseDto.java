package com.chone.server.domains.user.dto.response;

import com.chone.server.domains.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String role;
    private LocalDateTime createdAt;

    public static SearchUserResponseDto fromEntity(User user) {
        return new SearchUserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}
