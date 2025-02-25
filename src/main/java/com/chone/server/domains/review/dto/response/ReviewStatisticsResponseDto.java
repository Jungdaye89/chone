package com.chone.server.domains.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "리뷰 통계 응답 DTO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReviewStatisticsResponseDto {

  @Schema(description = "평균 평점", example = "4.2")
  private BigDecimal averageRating;

  @Schema(description = "전체 리뷰 개수", example = "150")
  private int totalReviews;

  @Schema(
      description = "평점 분포 (예: 별점 5개 70개, 별점 4개 50개)",
      example = "{5: 70, 4: 50, 3: 20, 2: 5, 1: 5}")
  private Map<Integer, Integer> ratingDistribution;

  @Schema(description = "통계 업데이트 시간", example = "2025-02-22T10:30:00")
  private LocalDateTime lastUpdatedAt;
}
