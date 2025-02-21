package com.chone.server.commons.facade;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.user.domain.User;
import java.util.List;

public interface OrderFacade {
  List<Order> findAllOrderByUserId(Long userId);

  void deleteOrder(User user, Order order);

  void validateOrderOwnership(Order order, User currentUser);

  void validateOrderStatus(Order order, OrderStatus status);
}
