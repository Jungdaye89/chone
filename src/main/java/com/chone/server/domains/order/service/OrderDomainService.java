package com.chone.server.domains.order.service;

import static org.springframework.util.StringUtils.hasText;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderItem;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.order.dto.request.CreateOrderRequest;
import com.chone.server.domains.order.dto.request.CreateOrderRequest.OrderItemRequest;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderDomainService {
  private static final int ORDER_CANCELLATION_TIME_LIMIT_MINUTES = 5;

  public void validateStoreOwnershipIfRoleOwner(User user, Store store) {
    if (user.getRole().isOwner() && !store.getUser().equals(user)) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_STORE_OWNER_ACCESS_DENIED);
    }
  }

  public void validateStoreAndProducts(Store store, List<Product> products) {
    if (products.stream().anyMatch(product -> !product.getStore().equals(store))) {
      throwApiException(OrderExceptionCode.MULTIPLE_STORE_ORDER);
    }
  }

  public OrderType determineOrderType(Role userRole, CreateOrderRequest requestDto) {
    return switch (userRole) {
      case OWNER -> OrderType.OFFLINE;
      case CUSTOMER -> {
        if (!hasText(requestDto.address()))
          throwApiException(OrderExceptionCode.MISSING_DELIVERY_ADDRESS);
        yield OrderType.ONLINE;
      }
      default -> throw new ApiBusinessException(OrderExceptionCode.FORBIDDEN_ORDER);
    };
  }

  public Order createOrder(
      Store store,
      User user,
      List<OrderItemRequest> itemRequests,
      List<Product> products,
      OrderType orderType,
      String address,
      String requestText) {

    validateStoreOperationStatus(store);
    Map<UUID, Product> productMap = validateAndGetProductMap(itemRequests, products);
    int totalPrice = calculateTotalPrice(productMap, itemRequests);

    Order order =
        Order.builder(store, user, orderType, totalPrice, OrderStatus.PENDING)
            .request(requestText)
            .address(address)
            .build();

    List<OrderItem> orderItems = createOrderItems(order, productMap, itemRequests);
    order.addOrderItem(orderItems);

    return order;
  }

  public void validateOrderStatusChangePermission(User user, Order order) {
    if (user.getRole().isOwner()) {
      if (!order.getStore().getUser().equals(user)) {
        throw new ApiBusinessException(OrderExceptionCode.ORDER_STATUS_CHANGE_NOT_OWNER);
      }
    }
    if (user.getRole().isCustomer()) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_STATUS_CHANGE_FORBIDDEN);
    }
  }

  public void validateStatusChange(Order order, OrderStatus currentStatus, OrderStatus newStatus) {
    if (newStatus.isSameStatus(OrderStatus.CANCELED))
      throwApiException(OrderExceptionCode.ORDER_CANCEL_SEPARATE_API);

    if (newStatus.isSameStatus(OrderStatus.PAID))
      throwApiException(OrderExceptionCode.ORDER_PAID_SEPARATE);

    if (currentStatus.isTerminal())
      throwApiException(OrderExceptionCode.ORDER_FINALIZED_STATE_CONFLICT);

    if (!newStatus.isProgressableFrom(currentStatus))
      throwApiException(OrderExceptionCode.ORDER_STATUS_REGRESSION);

    if (order.getOrderType().isOffline() && !newStatus.isValidForOfflineOrder())
      throwApiException(OrderExceptionCode.OFFLINE_ORDER_DELIVERY_STATUS);
  }

  private void validateStoreOperationStatus(Store store) {
    if (!store.getIsOpen()) throwApiException(OrderExceptionCode.ORDER_STORE_CLOSED);
  }

  private Map<UUID, Product> validateAndGetProductMap(
      List<OrderItemRequest> itemRequests, List<Product> products) {
    if (products.size() != itemRequests.size())
      throwApiException(OrderExceptionCode.ORDER_PRODUCT_MISMATCH);

    if (products.isEmpty()) throwApiException(OrderExceptionCode.ORDER_PRODUCT_MISMATCH);

    Map<UUID, Product> productMap =
        products.stream().collect(Collectors.toMap(Product::getId, product -> product));
    products.forEach(this::validateProduct);

    return productMap;
  }

  private void validateProduct(Product product) {
    if (!product.getIsAvailable()) throwApiException(OrderExceptionCode.ORDER_PRODUCT_UNAVAILABLE);
  }

  private int calculateTotalPrice(Map<UUID, Product> productMap, List<OrderItemRequest> items) {
    return items.stream()
        .mapToInt(
            item -> {
              Product product = productMap.get(item.productId());
              return product.getPrice() * item.quantity();
            })
        .sum();
  }

  private List<OrderItem> createOrderItems(
      Order order, Map<UUID, Product> productMap, List<OrderItemRequest> items) {
    return items.stream()
        .map(
            item -> {
              Product product = productMap.get(item.productId());
              int itemPrice = product.getPrice() * item.quantity();
              return OrderItem.builder(order, product, item.quantity(), itemPrice).build();
            })
        .toList();
  }

  void validateCancellationPermission(User user, Order order) {
    switch (user.getRole()) {
      case MANAGER, MASTER -> {
        return;
      }
      case CUSTOMER -> {
        if (!order.getUser().equals(user)) {
          throw new ApiBusinessException(OrderExceptionCode.ORDER_CUSTOMER_ACCESS_DENIED);
        }
        return;
      }
      case OWNER -> {
        if (!order.getStore().getUser().equals(user)) {
          throw new ApiBusinessException(OrderExceptionCode.ORDER_STORE_OWNER_ACCESS_DENIED);
        }
        return;
      }
    }
    throw new ApiBusinessException(OrderExceptionCode.ORDER_CANCEL_PERMISSION_DENIED);
  }

  public void validateCancellation(Order order) {
    if (order.getStatus() == OrderStatus.CANCELED)
      throwApiException(OrderExceptionCode.ORDER_ALREADY_CANCELED);

    if (order.getStatus() == OrderStatus.IN_DELIVERY)
      throwApiException(OrderExceptionCode.ORDER_ALREADY_IN_DELIVERY);

    if (order.getStatus() == OrderStatus.COMPLETED)
      throwApiException(OrderExceptionCode.ORDER_ALREADY_COMPLETED);

    if (!order.isCancelable()) throwApiException(OrderExceptionCode.ORDER_NOT_CANCELABLE);
  }

  public boolean isAfterCancellationTimeLimit(Order order) {
    LocalDateTime orderCreatedAt = order.getCreatedAt();
    LocalDateTime cancellationDeadline =
        orderCreatedAt.plusMinutes(ORDER_CANCELLATION_TIME_LIMIT_MINUTES);

    return LocalDateTime.now().isAfter(cancellationDeadline);
  }

  public boolean isDeletableOrder(OrderStatus status) {
    return status.isSameStatus(OrderStatus.CANCELED) || status.isSameStatus(OrderStatus.COMPLETED);
  }

  private void throwApiException(OrderExceptionCode exceptionCode) {
    throw new ApiBusinessException(exceptionCode);
  }
}
