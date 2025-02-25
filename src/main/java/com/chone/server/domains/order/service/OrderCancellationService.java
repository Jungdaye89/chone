package com.chone.server.domains.order.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.dto.request.CancelOrderRequest;
import com.chone.server.domains.order.dto.response.CancelOrderResponse;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderRepository;
import com.chone.server.domains.user.domain.User;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderCancellationService {

  private final OrderRepository repository;
  private final OrderDomainService domainService;
  private final ApplicationEventPublisher eventPublisher;

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

    eventPublisher.publishEvent(new OrderCancelledEvent(savedOrder, true));
    log.info(
        "주문 취소 이벤트 발행: orderId={}, reason={}", savedOrder.getId(), savedOrder.getCancelReason());

    return CancelOrderResponse.from(savedOrder);
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
}
