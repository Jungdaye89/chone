package com.chone.server.domains.order.service;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.order.dto.request.CreateOrderRequest;
import com.chone.server.domains.order.dto.request.CreateOrderRequest.OrderItemRequest;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderDomainService {
  public void validateStoreAndProducts(Store store, List<Product> products) {}

  public Order createOrder(
      Store store,
      User user,
      List<OrderItemRequest> itemRequests,
      List<Product> products,
      OrderType orderType) {

    return null;
  }

  public OrderType determineOrderType(Role userRole, CreateOrderRequest requestDto) {
    return null;
  }
}
