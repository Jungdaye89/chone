package com.chone.server.domains.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class UpdateRequestDto {

  private String name;
  private double price;
  private String imageUrl;
  private String description;
  private boolean isAvailable;
}