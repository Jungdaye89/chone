package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderItem;
import com.chone.server.domains.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Schema(description = "주문 생성 응답")
public record CreateOrderResponse(
    @Schema(
            description = "주문 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        UUID id,
    @Schema(description = "주문 상세 정보", required = true) OrderResponse order,
    @Schema(description = "가게 정보", required = true) StoreResponse store,
    @Schema(description = "주문 상품 목록", required = true) List<OrderItemResponse> orderItem) {

  private CreateOrderResponse(Order order, Store store, List<OrderItem> orderItems) {
    this(
        order.getId(),
        new OrderResponse(
            order.getId(),
            order.getTotalPrice(),
            order.getOrderType().getDescription(),
            order.getStatus().getDescription(),
            order.getCreatedAt()),
        new StoreResponse(store.getName(), store.getAddress()),
        orderItems.stream()
            .map(
                orderItem ->
                    new OrderItemResponse(orderItem.getProduct().getName(), orderItem.getAmount()))
            .collect(Collectors.toList()));
  }

  public static CreateOrderResponse from(Order order) {
    return new CreateOrderResponse(order, order.getStore(), order.getOrderItems());
  }

  @Schema(description = "주문 상세 정보")
  private record OrderResponse(
      @Schema(
              description = "주문 ID",
              example = "123e4567-e89b-12d3-a456-426614174000",
              required = true)
          UUID orderId,
      @Schema(description = "총 주문 금액", example = "25000", required = true) int totalPrice,
      @Schema(description = "주문 유형", example = "배달", required = true) String orderType,
      @Schema(description = "주문 상태", example = "주문 접수", required = true) String status,
      @Schema(description = "주문 생성 시간", required = true) LocalDateTime createdAt) {}

  @Schema(description = "가게 정보")
  private record StoreResponse(
      @Schema(description = "가게 이름", example = "맛있는 피자", required = true) String name,
      @Schema(description = "가게 주소", example = "서울시 강남구 역삼동 123-45", required = true)
          String address) {}

  @Schema(description = "주문 상품 정보")
  private record OrderItemResponse(
      @Schema(description = "상품명", example = "페퍼로니 피자", required = true) String name,
      @Schema(description = "주문 금액", example = "25000", required = true) int amount) {}
}
