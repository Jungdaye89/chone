package com.chone.server.domains.order.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.DeliveryFacade;
import com.chone.server.commons.facade.ProductFacade;
import com.chone.server.commons.facade.StoreFacade;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Delivery;
import com.chone.server.domains.order.domain.DeliveryStatus;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.order.dto.request.CancelOrderRequest;
import com.chone.server.domains.order.dto.request.CreateOrderRequest;
import com.chone.server.domains.order.dto.request.CreateOrderRequest.OrderItemRequest;
import com.chone.server.domains.order.dto.request.OrderFilterParams;
import com.chone.server.domains.order.dto.request.OrderStatusUpdateRequest;
import com.chone.server.domains.order.dto.response.CancelOrderResponse;
import com.chone.server.domains.order.dto.response.CreateOrderResponse;
import com.chone.server.domains.order.dto.response.DeleteOrderResponse;
import com.chone.server.domains.order.dto.response.OrderDetailResponse;
import com.chone.server.domains.order.dto.response.OrderPageResponse;
import com.chone.server.domains.order.dto.response.OrderStatusUpdateResponse;
import com.chone.server.domains.order.dto.response.PageResponse;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderRepository;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.User;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderService {

  private final OrderRepository repository;
  private final OrderDomainService domainService;

  private final ProductFacade productFacade;
  private final StoreFacade storeFacade;
  private final DeliveryFacade deliveryFacade;

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

  public PageResponse<OrderPageResponse> getOrders(
      CustomUserDetails principal, OrderFilterParams filterParams, Pageable pageable) {

    User user = principal.getUser();

    return PageResponse.from(findOrdersByRole(user, filterParams, pageable));
  }

  public OrderDetailResponse getOrderById(CustomUserDetails principal, UUID id) {

    User user = principal.getUser();
    return switch (user.getRole()) {
      case CUSTOMER -> repository.findOrderByIdForCustomer(id, user);
      case OWNER -> repository.findOrderByIdForOwner(id, user);
      case MANAGER, MASTER -> repository.findOrderByIdForAdmin(id);
    };
  }

  public CancelOrderResponse cancelOrder(
      CustomUserDetails principal, UUID orderId, @Valid CancelOrderRequest requestDto) {

    Order order = repository.findForCancellationById(orderId);
    User currentUser = principal.getUser();

    domainService.validateCancellationPermission(currentUser, order);
    domainService.validateCancellation(order);

    boolean isAfterDeadline = domainService.isAfterCancellationTimeLimit(order);
    if (isAfterDeadline) {
      updateNotCancelable(order);
      throw new ApiBusinessException(OrderExceptionCode.ORDER_CANCELLATION_TIMEOUT);
    }

    Order savedOrder = updateAndSaveOrder(order, () -> order.cancel(requestDto.reasonNum()));

    // TODO: 1. 결제 -> listener
    //       2. 배달 -> listener
    return CancelOrderResponse.from(savedOrder);
  }

  @Transactional
  public DeleteOrderResponse deleteOrder(CustomUserDetails principal, UUID id) {

    Order order = findByOrderId(id);
    if (!domainService.isDeletableOrder(order.getStatus())) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_NOT_DELETABLE);
    }
    order.softDelete(principal.getUser());
    repository.save(order);

    // TODO: 1. 결제 -> listener
    //       2. 배달 -> listener
    return DeleteOrderResponse.from(order);
  }

  @Transactional
  public OrderStatusUpdateResponse updateOrderStatus(
      CustomUserDetails principal, UUID id, OrderStatusUpdateRequest request) {
    Order order = repository.findById(id);

    OrderStatus newStatus = request.status();
    OrderStatus currentStatus = order.getStatus();

    domainService.validateOrderStatusChangePermission(principal.getUser(), order);
    domainService.validateStatusChange(order, currentStatus, newStatus);

    order.updateStatus(newStatus);
    processAdditionalLogic(order, newStatus);

    Order savedOrder = repository.save(order);
    return OrderStatusUpdateResponse.from(savedOrder);
  }

  public Order findByOrderId(UUID orderId) {

    return repository.findById(orderId);
  }

  private Page<OrderPageResponse> findOrdersByRole(
      User user, OrderFilterParams filterParams, Pageable pageable) {

    return switch (user.getRole()) {
      case CUSTOMER -> repository.findOrdersByCustomer(user, filterParams, pageable);
      case OWNER -> repository.findOrdersByOwner(user, filterParams, pageable);
      case MANAGER, MASTER -> repository.findOrdersByAdmin(user, filterParams, pageable);
    };
  }

  private void processAdditionalLogic(Order order, OrderStatus newStatus) {
    log.info("주문 상태 변경: {} -> {}", order.getStatus().getDescription(), newStatus.getDescription());
    int step = newStatus.getStep();
    if (step >= OrderStatus.FOOD_PREPARING.getStep()) {
      order.updateNotCancelable();
    }
    if (step == OrderStatus.AWAITING_DELIVERY.getStep()) handleAwaitingDelivery(order);
    if (step == OrderStatus.IN_DELIVERY.getStep()) handleInDelivery(order);
    if (step == OrderStatus.COMPLETED.getStep()) handleOrderCompletion(order);
  }

  private void handleAwaitingDelivery(Order order) {
    Delivery existingDelivery = deliveryFacade.findByOrderOrNull(order);

    if (existingDelivery == null) {
      Delivery delivery =
          Delivery.builder(order, DeliveryStatus.PENDING, order.getAddress()).build();
      deliveryFacade.save(delivery);
    } else if (existingDelivery.getStatus() != DeliveryStatus.PENDING) {
      existingDelivery.updateStatus(DeliveryStatus.PENDING);
      deliveryFacade.save(existingDelivery);
    }
  }

  private void handleInDelivery(Order order) {
    Delivery delivery = deliveryFacade.findByOrder(order);

    delivery.updateStatus(DeliveryStatus.PICKED_UP);

    deliveryFacade.save(delivery);
  }

  private void handleOrderCompletion(Order order) {
    if (order.getOrderType() == OrderType.ONLINE) {
      try {
        Delivery delivery = deliveryFacade.findByOrder(order);
        delivery.updateStatus(DeliveryStatus.COMPLETED);
        deliveryFacade.save(delivery);
      } catch (Exception e) {
        log.warn("온라인 주문({})에 배달 정보가 없습니다.", order.getId());
      }
    }
  }

  @Transactional
  void updateNotCancelable(Order order) {

    order.updateNotCancelable();
    repository.save(order);
  }

  @Transactional
  Order updateAndSaveOrder(Order order, Runnable updateAction) {

    updateAction.run();
    return repository.save(order);
  }

  public Order saveOrder(Order order) {

    return repository.save(order);
  }
}
