package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.Order;
import java.time.LocalDateTime;

public record DeleteOrderResponse(LocalDateTime deletedRequestedAt) {
  private DeleteOrderResponse(Order order) {
    this(order.getDeletedAt());
  }

  public static DeleteOrderResponse from(Order order) {
    return new DeleteOrderResponse(order);
  }
}
