package com.chone.server.domains.order.repository;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.dto.request.OrderFilterParams;
import com.chone.server.domains.order.dto.response.OrderPageResponse;
import com.chone.server.domains.user.domain.User;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {
  Order save(Order order);

  Order findById(UUID uuid);

  Page<OrderPageResponse> findOrdersByCustomer(
      User customer, OrderFilterParams filterParams, Pageable pageable);

  Page<OrderPageResponse> findOrdersByOwner(
      User owner, OrderFilterParams filterParams, Pageable pageable);

  Page<OrderPageResponse> findOrdersByAdmin(
      User admin, OrderFilterParams filterParams, Pageable pageable);
}
