package com.chone.server.domains.user.dto.response;

import com.chone.server.domains.user.domain.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchUserResponseDto {
    private List<ReadResponseDto> content;
    private PageInfoDto pageInfo;
}
