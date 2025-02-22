package com.chone.server.domains.delivery.infrastructure;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.delivery.exception.DeliveryExceptionCode;
import com.chone.server.domains.delivery.repsitory.DeliveryRepository;
import com.chone.server.domains.order.domain.Delivery;
import com.chone.server.domains.order.domain.Order;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {
  private final DeliveryJpaRepository jpaRepository;

  @Override
  public Delivery save(Delivery delivery) {
    return jpaRepository.save(delivery);
  }

  @Override
  public Delivery findById(UUID id) {
    return jpaRepository
        .findByIdAndDeletedAtNull(id)
        .orElseThrow(() -> new ApiBusinessException(DeliveryExceptionCode.NOT_FOUND_DELIVERY));
  }

  @Override
  public Delivery findByOrder(Order order) {
    return jpaRepository
        .findByOrderAndDeletedAtNull(order)
        .orElseThrow(() -> new ApiBusinessException(DeliveryExceptionCode.NOT_FOUND_DELIVERY));
  }

  @Override
  public Delivery findByOrderOrNull(Order order) {
    return jpaRepository.findByOrderAndDeletedAtNull(order).orElse(null);
  }

  @Override
  public Delivery findByOrderIdOrNull(UUID id) {
    return jpaRepository.findByOrderIdOrNull(id);
  }
}
