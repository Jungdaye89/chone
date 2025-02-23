package com.chone.server.domains.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Schema(description = "주문 request body")
public record CreateOrderRequest(
    @NotNull(message = "가게 ID는 필수입니다") UUID storeId,
    @NotEmpty(message = "주문 상품은 최소 1개 이상이어야 합니다") List<@Valid OrderItemRequest> orderItems,
    String address,
    String requestText) {
  public record OrderItemRequest(
      @NotNull(message = "상품 ID는 필수입니다") UUID productId,
      @Min(value = 1, message = "수량은 1개 이상이어야 합니다") @NotNull(message = "수량은 필수입니다")
          Integer quantity) {}
}
