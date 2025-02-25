package com.chone.server.commons.facade;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.user.domain.User;
import java.util.List;
import java.util.UUID;

public interface OrderFacade {
  List<Order> findAllOrderByUserId(Long userId);

  void deleteOrder(User user, Order order);

  void validateOrderOwnership(Order order, User currentUser);

  void validateOrderStatus(Order order, OrderStatus status);

  Order findById(UUID uuid);

  Order save(Order order);
}
