package com.chone.server.domains.product.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class UpdateRequestDto {

  private String name;
  private int price;
  @Setter
  private String imageUrl;
  private String description;
  private Boolean isAvailable;
}