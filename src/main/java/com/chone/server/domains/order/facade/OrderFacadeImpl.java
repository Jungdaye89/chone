package com.chone.server.domains.order.facade;

import com.chone.server.commons.facade.OrderFacade;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.repository.OrderRepository;
import com.chone.server.domains.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
}
