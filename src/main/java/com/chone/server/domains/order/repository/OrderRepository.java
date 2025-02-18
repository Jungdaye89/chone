package com.chone.server.domains.order.repository;

import com.chone.server.domains.order.domain.Order;
import java.util.UUID;

public interface OrderRepository {
  Order save(Order order);

  Order findById(UUID uuid);
}
