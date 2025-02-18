package com.chone.server.domains.review.dto.response;

import com.chone.server.domains.review.domain.Review;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReviewReadResponseDto {

  private UUID reviewId;
  private UUID orderId;
  private UUID storeId;
  private Long customerId;
  private BigDecimal rating;
  private String content;
  private String imageUrl;
  private LocalDateTime writtenAt;

  public static ReviewReadResponseDto from(Review review) {

    return ReviewReadResponseDto.builder()
        .reviewId(review.getId())
        .orderId(review.getOrder().getId())
        .storeId(review.getStore().getId())
        .customerId(review.getUser().getId())
        .rating(review.getRating())
        .content(review.getContent())
        .imageUrl(review.getImageUrl())
        .writtenAt(review.getCreatedAt())
        .build();
  }
}
