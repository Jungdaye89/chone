package com.chone.server.domains.order.facade;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.OrderFacade;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderRepository;
import com.chone.server.domains.user.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderFacadeImpl implements OrderFacade {
  private final OrderRepository orderRepository;

  @Override
  public List<Order> findAllOrderByUserId(Long userId) {
    return orderRepository.findOrderByUserId(userId);
  }

  @Override
  public void deleteOrder(User user, Order order) {
    order.delete(user);
  }

  @Override
  public void validateOrderOwnership(Order order, User currentUser) {
    User orderOwnerUser = order.getUser();
    if (orderOwnerUser.equals(currentUser))
      throw new ApiBusinessException(OrderExceptionCode.ORDER_CUSTOMER_ACCESS_DENIED);
  }

  @Override
  public void validateOrderStatus(Order order, OrderStatus status) {
    if (!order.getStatus().isSameStatus(status))
      throw new ApiBusinessException(OrderExceptionCode.ORDER_STATUS_MISMATCH);
  }
}
