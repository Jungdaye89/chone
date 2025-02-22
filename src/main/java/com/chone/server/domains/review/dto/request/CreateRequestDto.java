package com.chone.server.domains.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "리뷰 생성 요청 DTO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class CreateRequestDto {

  @Schema(description = "주문 ID", example = "acb12345-6789-0abc-def1-234567890abc")
  private UUID orderId;

  @Schema(description = "가게 ID", example = "e4d1f5d2-3c89-4b99-aeef-2b4665d707a3")
  private UUID storeId;

  @Schema(description = "리뷰 내용", example = "이 가게의 서비스가 매우 좋았습니다.")
  private String content;

  @Schema(description = "리뷰 평점 (1.0 ~ 5.0)", example = "4.5")
  private BigDecimal rating;

  @Schema(
      description = "리뷰 이미지 URL",
      example = "https://mybucket.s3.ap-northeast-2.amazonaws.com/images/product123.jpg")
  private String imageUrl;
}
