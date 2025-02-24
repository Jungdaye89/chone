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
@Schema(description = "상품 조회 응답 Dto")
public class ReadResponseDto {

  @Schema(description = "상품 ID")
  private UUID productId;

  @Schema(description = "상품 이름")
  private String name;

  @Schema(description = "상품 설명")
  private String description;

  @Schema(description = "상품 가격")
  private double price;

  @Schema(description = "이미지 URL")
  private String imageUrl;

  @Schema(description = "판매 가능 여부")
  private Boolean isAvailable;

  public static ReadResponseDto from(Product product) {

    return ReadResponseDto.builder()
        .productId(product.getId())
        .name(product.getName())
        .description(product.getDescription())
        .price(product.getPrice())
        .imageUrl(product.getImageUrl())
        .isAvailable(product.getIsAvailable())
        .build();
  }
}