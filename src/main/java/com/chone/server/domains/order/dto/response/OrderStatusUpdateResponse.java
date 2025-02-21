package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.Order;
import java.time.LocalDateTime;
import java.util.UUID;

public record OrderStatusUpdateResponse(
        UUID orderId, String type, String status, LocalDateTime updatedAt) {

  private OrderStatusUpdateResponse(Order order) {
      this(
              order.getId(),
              order.getOrderType().getDescription(),
              order.getStatus().getDescription(),
              order.getUpdatedAt()
      );
  }
  public static OrderStatusUpdateResponse from(Order order){
      return new OrderStatusUpdateResponse(order);
  }
}
