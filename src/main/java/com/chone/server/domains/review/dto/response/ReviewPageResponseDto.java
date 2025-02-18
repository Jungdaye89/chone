package com.chone.server.domains.review.dto.response;

import com.chone.server.domains.review.domain.Review;
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
public class ReviewPageResponseDto {

  private UUID reviewId;
  private UUID orderId;
  private UUID storeId;
  private Long customerId;
  private String content;
  private String imageUrl;
  private Double rating;
  private LocalDateTime writtenAt;

  public static ReviewPageResponseDto from(Review review) {

    return ReviewPageResponseDto.builder()
        .reviewId(review.getId())
        .orderId(review.getOrder().getId())
        .storeId(review.getStore().getId())
        .customerId(review.getUser().getId())
        .content(review.getContent())
        .imageUrl(review.getImageUrl())
        .rating(review.getRating().doubleValue())
        .writtenAt(review.getCreatedAt())
        .build();
  }
}
