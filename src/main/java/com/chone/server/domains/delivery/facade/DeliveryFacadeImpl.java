package com.chone.server.domains.delivery.facade;

import com.chone.server.commons.facade.DeliveryFacade;
import com.chone.server.domains.delivery.repsitory.DeliveryRepository;
import com.chone.server.domains.order.domain.Delivery;
import com.chone.server.domains.order.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeliveryFacadeImpl implements DeliveryFacade {
  private final DeliveryRepository repository;

  @Override
  public Delivery save(Delivery delivery) {
    return repository.save(delivery);
  }

  @Override
  public Delivery findByOrder(Order order) {
    return repository.findByOrder(order);
  }

  @Override
  public Delivery findByOrderOrNull(Order order) {
    return repository.findByOrderOrNull(order);
  }
}
