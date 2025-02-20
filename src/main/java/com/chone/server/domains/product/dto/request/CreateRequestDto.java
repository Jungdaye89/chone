package com.chone.server.domains.product.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class CreateRequestDto {

  private UUID storeId;
  private String name;
  private double price;
  @Setter
  private String imageUrl;
  private String description;
}