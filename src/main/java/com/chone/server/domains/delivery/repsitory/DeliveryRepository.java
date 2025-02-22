package com.chone.server.domains.delivery.repsitory;

import com.chone.server.domains.order.domain.Delivery;
import com.chone.server.domains.order.domain.Order;
import java.util.UUID;

public interface DeliveryRepository {

  Delivery save(Delivery delivery);

  Delivery findById(UUID id);

  Delivery findByOrder(Order order);

  Delivery findByOrderOrNull(Order order);

  Delivery findByOrderIdOrNull(UUID id);
}
