package com.chone.server.domains.order.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.dto.response.DeleteOrderResponse;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderDeletionService {

  private final OrderRepository repository;
  private final OrderDomainService domainService;

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

  public Order findByOrderId(UUID orderId) {

    return repository.findById(orderId);
  }
}
