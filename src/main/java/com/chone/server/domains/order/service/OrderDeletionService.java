package com.chone.server.domains.order.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.dto.response.DeleteOrderResponse;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderRepository;
import com.chone.server.domains.user.domain.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderDeletionService {

  private final OrderRepository repository;
  private final OrderDomainService domainService;

  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public DeleteOrderResponse deleteOrder(CustomUserDetails principal, UUID id) {
    Order order = repository.findById(id);
    if (!domainService.isDeletableOrder(order.getStatus())) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_NOT_DELETABLE);
    }

    User user = principal.getUser();
    order.softDelete(user);
    Order savedOrder = repository.save(order);

    eventPublisher.publishEvent(new OrderDeletedEvent(savedOrder, user, true));
    log.info("주문 삭제 이벤트 발행: orderId={}", savedOrder.getId());

    return DeleteOrderResponse.from(savedOrder);
  }
}
