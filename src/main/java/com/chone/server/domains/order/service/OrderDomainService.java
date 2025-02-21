package com.chone.server.domains.order.service;

import static org.springframework.util.StringUtils.hasText;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.common.vo.Price;
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

  public void validateStoreAndProducts(Store store, List<Product> products) {
    UUID storeId = store.getId();
    if (products.stream().anyMatch(product -> !product.getStore().getId().equals(storeId))) {
      throw new ApiBusinessException(OrderExceptionCode.MULTIPLE_STORE_ORDER);
    }
  }

  public OrderType determineOrderType(Role userRole, CreateOrderRequest requestDto) {
    return switch (userRole) {
      case OWNER -> OrderType.ONLINE;
      case CUSTOMER -> {
        if (!hasText(requestDto.address())) {
          throw new ApiBusinessException(OrderExceptionCode.MISSING_DELIVERY_ADDRESS);
        }
        yield OrderType.OFFLINE;
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
      String requestText) {

    validateStoreOperationStatus(store);
    Map<UUID, Product> productMap = validateAndGetProductMap(itemRequests, products);
    Price totalPrice = calculateTotalPrice(productMap, itemRequests);

    Order order =
        Order.builder(store, user, orderType, totalPrice.value(), OrderStatus.PENDING)
            .request(requestText)
            .build();

    List<OrderItem> orderItems = createOrderItems(order, productMap, itemRequests);
    order.addOrderItem(orderItems);

    return order;
  }

  private void validateStoreOperationStatus(Store store) {
    if (!store.getIsOpen()) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_STORE_CLOSED);
    }
  }

  private Map<UUID, Product> validateAndGetProductMap(
      List<OrderItemRequest> itemRequests, List<Product> products) {
    if (products.size() != itemRequests.size()) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_PRODUCT_MISMATCH);
    }

    if (products.isEmpty()) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_PRODUCT_MISMATCH);
    }

    Map<UUID, Product> productMap =
        products.stream().collect(Collectors.toMap(Product::getId, product -> product));

    products.forEach(this::validateProduct);

    return productMap;
  }

  private void validateProduct(Product product) {
    if (!product.getIsAvailable()) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_PRODUCT_UNAVAILABLE);
    }
  }

  private Price calculateTotalPrice(Map<UUID, Product> productMap, List<OrderItemRequest> items) {
    return Price.sum(
        items.stream()
            .map(
                item -> {
                  Product product = productMap.get(item.productId());
                  return new Price(product.getPrice()).multiply(item.quantity());
                })
            .toList());
  }

  private List<OrderItem> createOrderItems(
      Order order, Map<UUID, Product> productMap, List<OrderItemRequest> items) {
    return items.stream()
        .map(
            item -> {
              Product product = productMap.get(item.productId());
              Price itemPrice = new Price(product.getPrice()).multiply(item.quantity());
              return OrderItem.builder(order, product, item.quantity(), itemPrice.value()).build();
            })
        .toList();
  }

  void validateCancellationPermission(User user, Order order) {
    switch (user.getRole()) {
      case MANAGER, MASTER -> {
        return;
      }
      case CUSTOMER -> {
        if (!order.getUser().getId().equals(user.getId())) {
          throw new ApiBusinessException(OrderExceptionCode.ORDER_CUSTOMER_ACCESS_DENIED);
        }
        return;
      }
      case OWNER -> {
        if (!order.getStore().getUser().getId().equals(user.getId())) {
          throw new ApiBusinessException(OrderExceptionCode.ORDER_STORE_OWNER_ACCESS_DENIED);
        }
        return;
      }
    }
    throw new ApiBusinessException(OrderExceptionCode.ORDER_CANCEL_PERMISSION_DENIED);
  }

  public void validateCancellation(Order order) {
    if (order.getStatus() == OrderStatus.CANCELED) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_ALREADY_CANCELED);
    }

    if (!order.isCancelable()) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_NOT_CANCELABLE);
    }
  }

  public boolean isAfterCancellationTimeLimit(Order order) {
    LocalDateTime orderCreatedAt = order.getCreatedAt();
    LocalDateTime cancellationDeadline =
        orderCreatedAt.plusMinutes(ORDER_CANCELLATION_TIME_LIMIT_MINUTES);

    return LocalDateTime.now().isAfter(cancellationDeadline);
  }

  public boolean isDeletableOrder(OrderStatus status) {
    return status.equals(OrderStatus.CANCELED) || status.equals(OrderStatus.COMPLETED);
  }
}
