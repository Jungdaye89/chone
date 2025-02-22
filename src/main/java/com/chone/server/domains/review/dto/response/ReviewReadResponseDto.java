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

@Schema(description = "리뷰 조회 응답 DTO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReviewReadResponseDto {

  @Schema(description = "리뷰 ID", example = "d9b12345-6789-0abc-def1-234567890abc")
  private UUID reviewId;

  @Schema(description = "주문 ID", example = "acb12345-6789-0abc-def1-234567890abc")
  private UUID orderId;

  @Schema(description = "가게 ID", example = "e4d1f5d2-3c89-4b99-aeef-2b4665d707a3")
  private UUID storeId;

  @Schema(description = "고객 ID", example = "123456789")
  private Long customerId;

  @Schema(description = "리뷰 평점", example = "4.5")
  private BigDecimal rating;

  @Schema(description = "리뷰 내용", example = "서비스가 정말 좋았어요.")
  private String content;

  @Schema(
      description = "리뷰 이미지 URL",
      example = "https://mybucket.s3.ap-northeast-2.amazonaws.com/images/review123.jpg")
  private String imageUrl;

  @Schema(description = "작성 시간", example = "2025-02-22T10:30:00")
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
