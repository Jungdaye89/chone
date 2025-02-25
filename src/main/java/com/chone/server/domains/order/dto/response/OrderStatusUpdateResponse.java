package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "주문 상태 변경 응답")
public record OrderStatusUpdateResponse(
    @Schema(
            description = "주문 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        UUID orderId,
    @Schema(description = "주문 유형", example = "배달", required = true) String type,
    @Schema(description = "변경된 주문 상태", example = "주문 접수", required = true) String status,
    @Schema(description = "상태 변경 시간", required = true) LocalDateTime updatedAt) {

  private OrderStatusUpdateResponse(Order order) {
    this(
        order.getId(),
        order.getOrderType().getDescription(),
        order.getStatus().getDescription(),
        order.getUpdatedAt());
  }

  public static OrderStatusUpdateResponse from(Order order) {
    return new OrderStatusUpdateResponse(order);
  }
}
