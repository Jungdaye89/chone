package com.chone.server.domains.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Schema(description = "리뷰 목록 응답 DTO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReviewListResponseDto {

  @Schema(description = "리뷰 목록")
  private List<ReviewPageResponseDto> content;

  @Schema(description = "페이지 정보")
  private PageInfoDto pageInfo;

  public static ReviewListResponseDto from(Page<ReviewPageResponseDto> page) {
    return ReviewListResponseDto.builder()
        .content(page.getContent())
        .pageInfo(PageInfoDto.from(page))
        .build();
  }
}
