package com.chone.server.domains.order.service;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.dto.request.OrderFilterParams;
import com.chone.server.domains.order.dto.response.OrderDetailResponse;
import com.chone.server.domains.order.dto.response.OrderPageResponse;
import com.chone.server.domains.order.dto.response.PageResponse;
import com.chone.server.domains.order.repository.OrderRepository;
import com.chone.server.domains.user.domain.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class OrderReadService {
  private final OrderRepository repository;

  public PageResponse<OrderPageResponse> getOrders(
      CustomUserDetails principal, OrderFilterParams filterParams, Pageable pageable) {

    User user = principal.getUser();

    return PageResponse.from(findOrdersByRole(user, filterParams, pageable));
  }

  public OrderDetailResponse getOrderById(CustomUserDetails principal, UUID id) {

    User user = principal.getUser();
    return switch (user.getRole()) {
      case CUSTOMER -> repository.findOrderByIdForCustomer(id, user);
      case OWNER -> repository.findOrderByIdForOwner(id, user);
      case MANAGER, MASTER -> repository.findOrderByIdForAdmin(id);
    };
  }

  private Page<OrderPageResponse> findOrdersByRole(
      User user, OrderFilterParams filterParams, Pageable pageable) {

    return switch (user.getRole()) {
      case CUSTOMER -> repository.findOrdersByCustomer(user, filterParams, pageable);
      case OWNER -> repository.findOrdersByOwner(user, filterParams, pageable);
      case MANAGER, MASTER -> repository.findOrdersByAdmin(user, filterParams, pageable);
    };
  }
}
