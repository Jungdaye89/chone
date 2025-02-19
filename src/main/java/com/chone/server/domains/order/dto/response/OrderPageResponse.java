package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
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
  public OrderPageResponse(
      UUID id,
      UUID storeId,
      String storeName,
      OrderType type,
      OrderStatus status,
      BigDecimal totalPrice,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this(
        id,
        storeId,
        storeName,
        type.getDescription(),
        status.getDescription(),
        totalPrice,
        createdAt,
        updatedAt);
  }
}
