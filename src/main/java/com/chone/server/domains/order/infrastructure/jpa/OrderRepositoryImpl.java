package com.chone.server.domains.order.infrastructure.jpa;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.dto.request.OrderFilterParams;
import com.chone.server.domains.order.dto.response.OrderDetailResponse;
import com.chone.server.domains.order.dto.response.OrderPageResponse;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderDetailSearchRepository;
import com.chone.server.domains.order.repository.OrderListSearchRepository;
import com.chone.server.domains.order.repository.OrderRepository;
import com.chone.server.domains.user.domain.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
  private final OrderJpaRepository jpaRepository;
  private final OrderListSearchRepository jpaListSearchRepository;
  private final OrderDetailSearchRepository jpaDetailSearchRepository;

  @Override
  public Order save(Order order) {
    return jpaRepository.save(order);
  }

  @Override
  public Order findById(UUID uuid) {
    return jpaRepository
        .findById(uuid)
        .orElseThrow(() -> new ApiBusinessException(OrderExceptionCode.NOT_FOUND_ORDER));
  }

  @Override
  public Page<OrderPageResponse> findOrdersByCustomer(
      User customer, OrderFilterParams filterParams, Pageable pageable) {
    return jpaListSearchRepository.findOrdersByCustomer(customer, filterParams, pageable);
  }

  @Override
  public Page<OrderPageResponse> findOrdersByOwner(
      User owner, OrderFilterParams filterParams, Pageable pageable) {
    return jpaListSearchRepository.findOrdersByOwner(owner, filterParams, pageable);
  }

  @Override
  public Page<OrderPageResponse> findOrdersByAdmin(
      User admin, OrderFilterParams filterParams, Pageable pageable) {
    return jpaListSearchRepository.findOrdersByAdmin(admin, filterParams, pageable);
  }

  @Override
  public OrderDetailResponse findOrderByIdForCustomer(UUID orderId, User user) {
    return jpaDetailSearchRepository.findOrderDetailByIdForCustomer(orderId, user);
  }

  @Override
  public OrderDetailResponse findOrderByIdForOwner(UUID orderId, User user) {
    return jpaDetailSearchRepository.findOrderDetailByIdForOwner(orderId, user);
  }

  @Override
  public OrderDetailResponse findOrderByIdForAdmin(UUID orderId) {
    return jpaDetailSearchRepository.findOrderDetailByIdForAdmin(orderId);
  }

  @Override
  public Order findForCancellationById(UUID orderId) {
    return jpaRepository
        .findForCancellationById(orderId)
        .orElseThrow(() -> new ApiBusinessException(OrderExceptionCode.NOT_FOUND_ORDER));
  }
}
