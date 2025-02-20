package com.chone.server.domains.review.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReviewStatisticsResponseDto {

  private BigDecimal averageRating;
  private int totalReviews;
  private Map<Integer, Integer> ratingDistribution;
  private LocalDateTime lastUpdatedAt;
}
