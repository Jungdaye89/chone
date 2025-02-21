package com.chone.server.domains.order.service;

import com.chone.server.commons.facade.DeliveryFacade;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Delivery;
import com.chone.server.domains.order.domain.DeliveryStatus;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.order.dto.request.OrderStatusUpdateRequest;
import com.chone.server.domains.order.dto.response.OrderStatusUpdateResponse;
import com.chone.server.domains.order.repository.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderUpdateService {
  private final OrderRepository repository;
  private final OrderDomainService domainService;

  private final DeliveryFacade deliveryFacade;

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

  private void processAdditionalLogic(Order order, OrderStatus newStatus) {
    log.info("주문 상태 변경: {} -> {}", order.getStatus().getDescription(), newStatus.getDescription());

    if (newStatus.getStep() >= OrderStatus.FOOD_PREPARING.getStep()) order.updateNotCancelable();

    if (newStatus.isSameStatus(OrderStatus.AWAITING_DELIVERY)) handleAwaitingDelivery(order);
    if (newStatus.isSameStatus(OrderStatus.IN_DELIVERY)) handleInDelivery(order);
    if (newStatus.isSameStatus(OrderStatus.COMPLETED)) handleOrderCompletion(order);
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
}
