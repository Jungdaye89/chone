package com.chone.server.domains.order.dto.response;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderPageResponse(
    UUID id,
    UUID storeId,
    String storeName,
    String type,
    String status,
    BigDecimal totalPrice,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {

  @QueryProjection
  public OrderPageResponse {}
}
