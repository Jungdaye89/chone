package com.chone.server.domains.order.service;

import static org.springframework.util.StringUtils.hasText;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderItem;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.order.domain.vo.Price;
import com.chone.server.domains.order.dto.request.CreateOrderRequest;
import com.chone.server.domains.order.dto.request.CreateOrderRequest.OrderItemRequest;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderDomainService {
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
      OrderType orderType) {

    validateStoreOperationStatus(store);
    Map<UUID, Product> productMap = validateAndGetProductMap(itemRequests, products);
    Price totalPrice = calculateTotalPrice(productMap, itemRequests);

    Order order =
        Order.builder(store, user, orderType, totalPrice.value(), OrderStatus.PENDING)
            .orderItems(new ArrayList<>())
            .build();

    List<OrderItem> orderItems = createOrderItems(order, productMap, itemRequests);
    order.addOrderItem(orderItems);

    return order;
  }

  private void validateStoreOperationStatus(Store store) {
    if (!store.isOpen()) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_STORE_CLOSED);
    }
  }

  private Map<UUID, Product> validateAndGetProductMap(
      List<OrderItemRequest> itemRequests, List<Product> products) {
    return null;
  }

  private Price calculateTotalPrice(
      Map<UUID, Product> productMap, List<OrderItemRequest> itemRequests) {
    return null;
  }

  private List<OrderItem> createOrderItems(
      Order order, Map<UUID, Product> productMap, List<OrderItemRequest> itemRequests) {
    return null;
  }
}
