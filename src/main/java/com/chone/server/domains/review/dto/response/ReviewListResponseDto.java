package com.chone.server.domains.review.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReviewListResponseDto {

  private List<ReviewPageResponseDto> content;
  private PageInfoDto pageInfo;

  public static ReviewListResponseDto from(Page<ReviewPageResponseDto> page) {

    return ReviewListResponseDto.builder()
        .content(page.getContent())
        .pageInfo(PageInfoDto.from(page))
        .build();
  }
}
