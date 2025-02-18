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
public class ReadResponseDto {

  private UUID productId;
  private String name;
  private String description;
  private double price;
  private String imageUrl;
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