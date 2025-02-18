package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderItem;
import com.chone.server.domains.store.domain.Store;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public record CreateOrderResponse(
    OrderResponse order, StoreResponse store, List<OrderItemResponse> orderItem) {

  private CreateOrderResponse(Order order, Store store, List<OrderItem> orderItems) {
    this(
        new OrderResponse(
            order.getId(),
            order.getTotalPrice().intValue(),
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

  private record OrderResponse(
      UUID orderId, int totalPrice, String orderType, String status, LocalDateTime createdAt) {}

  private record StoreResponse(String name, String address) {}

  public record OrderItemResponse(String name, int amount) {}
}
