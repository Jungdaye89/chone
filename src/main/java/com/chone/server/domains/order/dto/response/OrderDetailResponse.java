package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record OrderDetailResponse(
    OrderResponse order,
    UserResponse user,
    StoreResponse store,
    List<OrderItemResponse> orderItems) {

  @QueryProjection
  public OrderDetailResponse {}

  public record OrderResponse(
      UUID id,
      OrderType type,
      OrderStatus status,
      BigDecimal totalPrice,
      String cancelReason,
      String request) {

    @QueryProjection
    public OrderResponse {}
  }

  public record UserResponse(Long id, String username) {

    @QueryProjection
    public UserResponse {}
  }

  public record StoreResponse(UUID id, String storeName) {
    @QueryProjection
    public StoreResponse {}
  }

  public record OrderItemResponse(UUID id, String productName, int amount) {
    @QueryProjection
    public OrderItemResponse {}
  }
}
