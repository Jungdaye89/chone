package com.chone.server.domains.product.dto.response;

import com.chone.server.domains.product.domain.Product;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class CreateResponseDto {

  private UUID storeId;
  private String name;
  private double price;
  private String imageUrl;
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