package com.chone.server.domains.review.dto.response;

import com.chone.server.domains.review.domain.Review;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "리뷰 상세 응답 DTO")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReviewDetailResponseDto {

  @Schema(description = "리뷰 ID", example = "d9b12345-6789-0abc-def1-234567890abc")
  private UUID reviewId;

  @Schema(description = "주문 ID", example = "acb12345-6789-0abc-def1-234567890abc")
  private UUID orderId;

  @Schema(description = "가게 정보")
  private StoreInfo storeInfo;

  @Schema(description = "고객 정보")
  private CustomerInfo customerInfo;

  @Schema(description = "리뷰 내용", example = "서비스가 정말 좋았어요.")
  private String content;

  @Schema(description = "리뷰 평점 (1.0 ~ 5.0)", example = "4.5")
  private BigDecimal rating;

  @Schema(
      description = "리뷰 이미지 URL",
      example = "https://mybucket.s3.ap-northeast-2.amazonaws.com/images/review123.jpg")
  private String imageUrl;

  @Schema(description = "작성 시간", example = "2025-02-22T10:30:00")
  private LocalDateTime writtenAt;

  @Schema(description = "공개 여부", example = "true")
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

  @Schema(description = "가게 정보 DTO")
  @Getter
  @AllArgsConstructor
  public static class StoreInfo {

    @Schema(description = "가게 ID", example = "e4d1f5d2-3c89-4b99-aeef-2b4665d707a3")
    private UUID storeId;

    @Schema(description = "가게 이름", example = "맛있는 식당")
    private String storeName;
  }

  @Schema(description = "고객 정보 DTO")
  @Getter
  @AllArgsConstructor
  public static class CustomerInfo {

    @Schema(description = "고객 ID", example = "123456789")
    private Long customerId;

    @Schema(description = "고객 이름", example = "johndoe")
    private String username;
  }
}
