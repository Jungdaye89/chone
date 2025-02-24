package com.chone.server.domains.product.dto.response;

import com.chone.server.domains.product.domain.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Schema(description = "상품 생성 응답 Dto")
public class CreateResponseDto {

  @Schema(description = "가게 ID")
  private UUID storeId;

  @Schema(description = "상품 이름")
  private String name;

  @Schema(description = "상품 가격")
  private double price;

  @Schema(description = "이미지 URL")
  private String imageUrl;

  @Schema(description = "상품 설명")
  private String description;

  public static CreateResponseDto from(Product product) {

    return CreateResponseDto.builder()
        .storeId(product.getStore().getId())
        .name(product.getName())
        .price(product.getPrice())
        .imageUrl(product.getImageUrl())
        .description(product.getDescription())
        .build();
  }
}