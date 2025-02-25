package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.Order;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "주문 삭제 응답")
public record DeleteOrderResponse(
    @Schema(description = "삭제 요청 시간", required = true) LocalDateTime deletedRequestedAt) {
  private DeleteOrderResponse(Order order) {
    this(order.getDeletedAt());
  }

  public static DeleteOrderResponse from(Order order) {
    return new DeleteOrderResponse(order);
  }
}
