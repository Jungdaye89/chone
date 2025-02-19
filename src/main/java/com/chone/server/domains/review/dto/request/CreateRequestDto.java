package com.chone.server.domains.review.dto.request;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class CreateRequestDto {

  private UUID orderId;
  private UUID storeId;
  private String content;
  private BigDecimal rating;
  private String imageUrl;
}
