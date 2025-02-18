package com.chone.server.domains.order.service;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.order.dto.request.CreateOrderRequest;
import com.chone.server.domains.order.dto.request.CreateOrderRequest.OrderItemRequest;
import com.chone.server.domains.order.dto.response.CreateOrderResponse;
import com.chone.server.domains.order.repository.OrderRepository;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.product.service.ProductService;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.service.StoreService;
import com.chone.server.domains.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {
  private final OrderRepository repository;

  private final OrderDomainService domainService;
  private final ProductService productService;
  private final StoreService storeService;

  @Transactional
  public CreateOrderResponse createOrder(CreateOrderRequest request, CustomUserDetails principal) {
    User user = principal.getUser();
    Store store = storeService.findStoreById(request.storeId());

    List<Product> products =
        productService.findAllById(
            request.orderItems().stream().map(OrderItemRequest::productId).toList());

    domainService.validateStoreAndProducts(store, products);

    OrderType orderType = domainService.determineOrderType(user.getRole(), request);
    Order order = domainService.createOrder(store, user, request.orderItems(), products, orderType);

    Order savedOrder = repository.save(order);

    // TODO: 1. 결제
    //       2. 배달

    return CreateOrderResponse.from(savedOrder);
  }
}
