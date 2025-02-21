package com.chone.server.commons.facade;

import com.chone.server.domains.order.domain.Delivery;
import com.chone.server.domains.order.domain.Order;

public interface DeliveryFacade {
  Delivery save(Delivery delivery);

  Delivery findByOrder(Order order);

  Delivery findByOrderOrNull(Order order);
}
