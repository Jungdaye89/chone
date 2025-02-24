package com.chone.server.domains.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Schema(description = "상품 수정 요청 Dto")
public class UpdateRequestDto {

  @Schema(description = "상품 이름")
  private String name;

  @Schema(description = "상품 가격")
  private int price;

  @Setter
  @Schema(description = "이미지 URL")
  private String imageUrl;

  @Schema(description = "상품 설명")
  private String description;

  @Schema(description = "판매 가능 여부")
  private Boolean isAvailable;
}