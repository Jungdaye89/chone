package com.chone.server.domains.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "리뷰 수정 요청 DTO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class UpdateRequestDto {

  @Schema(description = "수정할 리뷰 내용", example = "수정된 리뷰 내용입니다.")
  private String content;

  @Schema(description = "수정할 평점", example = "4.0")
  private BigDecimal rating;

  @Schema(
      description = "수정할 이미지 URL",
      example = "https://mybucket.s3.ap-northeast-2.amazonaws.com/images/product123.jpg")
  private String imageUrl;

  @Schema(description = "공개 여부", example = "true")
  private Boolean isPublic;
}
