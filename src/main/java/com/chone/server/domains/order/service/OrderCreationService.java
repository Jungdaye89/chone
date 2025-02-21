package com.chone.server.domains.order.service;

import com.chone.server.commons.facade.ProductFacade;
import com.chone.server.commons.facade.StoreFacade;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.order.dto.request.CreateOrderRequest;
import com.chone.server.domains.order.dto.request.CreateOrderRequest.OrderItemRequest;
import com.chone.server.domains.order.dto.response.CreateOrderResponse;
import com.chone.server.domains.order.repository.OrderRepository;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderCreationService {

  private final OrderRepository repository;
  private final OrderDomainService domainService;

  private final ProductFacade productFacade;
  private final StoreFacade storeFacade;

  @Transactional
  public CreateOrderResponse createOrder(
      @Valid CreateOrderRequest request, CustomUserDetails principal) {
    User user = principal.getUser();
    Store store = storeFacade.findStoreById(request.storeId());

    List<Product> products =
        productFacade.findAllById(
            request.orderItems().stream().map(OrderItemRequest::productId).toList());

    domainService.validateStoreAndProducts(store, products);

    OrderType orderType = domainService.determineOrderType(user.getRole(), request);
    Order order =
        domainService.createOrder(
            store,
            user,
            request.orderItems(),
            products,
            orderType,
            request.address(),
            request.requestText());

    Order savedOrder = repository.save(order);

    // TODO: 1. 결제
    //       2. 배달

    return CreateOrderResponse.from(savedOrder);
  }
}
