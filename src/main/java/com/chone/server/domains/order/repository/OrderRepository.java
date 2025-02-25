package com.chone.server.domains.order.repository;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.dto.request.OrderFilterParams;
import com.chone.server.domains.order.dto.response.OrderDetailResponse;
import com.chone.server.domains.order.dto.response.OrderPageResponse;
import com.chone.server.domains.user.domain.User;
import java.util.List;
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

  OrderDetailResponse findOrderByIdForCustomer(UUID orderId, User user);

  OrderDetailResponse findOrderByIdForOwner(UUID orderId, User user);

  OrderDetailResponse findOrderByIdForAdmin(UUID orderId);

  Order findForCancellationById(UUID orderId);

  List<Order> findOrderByUserId(Long userId);
}
