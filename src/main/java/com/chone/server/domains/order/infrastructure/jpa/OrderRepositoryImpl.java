package com.chone.server.domains.order.infrastructure.jpa;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
  private final OrderJpaRepository jpaRepository;

  @Override
  public Order save(Order order) {
    return jpaRepository.save(order);
  }

  @Override
  public Order findById(UUID uuid) {
    return jpaRepository
        .findById(uuid)
        .orElseThrow(() -> new ApiBusinessException(OrderExceptionCode.NOT_FOUND_ORDER));
  }
}
