package com.chone.server.domains.review.dto.response;

import com.chone.server.domains.review.domain.Review;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDetailResponseDto {

  private UUID reviewId;
  private UUID orderId;
  private StoreInfo storeInfo;
  private CustomerInfo customerInfo;
  private String content;
  private BigDecimal rating;
  private String imageUrl;
  private LocalDateTime writtenAt;
  private Boolean isPublic;

  public static ReviewDetailResponseDto from(Review review) {
    return ReviewDetailResponseDto.builder()
        .reviewId(review.getId())
        .orderId(review.getOrder().getId())
        .storeInfo(new StoreInfo(review.getStore().getId(), review.getStore().getName()))
        .customerInfo(new CustomerInfo(review.getUser().getId(), review.getUser().getUsername()))
        .content(review.getContent())
        .rating(review.getRating())
        .imageUrl(review.getImageUrl())
        .writtenAt(review.getCreatedAt())
        .isPublic(review.getIsPublic())
        .build();
  }

  @Getter
  @AllArgsConstructor
  public static class StoreInfo {

    private UUID storeId;
    private String storeName;
  }

  @Getter
  @AllArgsConstructor
  public static class CustomerInfo {

    private Long customerId;
    private String username;
  }
}
