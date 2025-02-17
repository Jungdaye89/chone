package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreateOrderResponse(UUID orderId, OrderStatus status, LocalDateTime createdAt) {
  private CreateOrderResponse(Order order) {
    this(order.getId(), order.getStatus(), order.getCreatedAt());
  }

  public static CreateOrderResponse from(Order order) {
    return new CreateOrderResponse(order);
  }
}
