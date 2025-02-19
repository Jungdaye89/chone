package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.Order;
import java.time.LocalDateTime;
import java.util.UUID;

public record CancelOrderResponse(UUID orderId, String status, LocalDateTime cancelRequestedAt) {
  private CancelOrderResponse(Order order) {
    this(order.getId(), order.getStatus().getDescription(), LocalDateTime.now());
  }

  public static CancelOrderResponse from(Order order) {
    return new CancelOrderResponse(order);
  }
}
