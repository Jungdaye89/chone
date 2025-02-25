package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "주문 취소 응답")
public record CancelOrderResponse(
    @Schema(
            description = "주문 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        UUID orderId,
    @Schema(description = "주문 상태", example = "주문 취소", required = true) String status,
    @Schema(description = "취소 요청 시간", required = true) LocalDateTime cancelRequestedAt) {
  private CancelOrderResponse(Order order) {
    this(order.getId(), order.getStatus().getDescription(), LocalDateTime.now());
  }

  public static CancelOrderResponse from(Order order) {
    return new CancelOrderResponse(order);
  }
}
