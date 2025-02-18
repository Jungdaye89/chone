package com.chone.server.domains.order.infrastructure.jpa;

import com.chone.server.domains.order.dto.response.OrderDetailResponse;
import com.chone.server.domains.order.repository.OrderDetailSearchRepository;
import com.chone.server.domains.user.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderDetailSearchRepositoryImpl implements OrderDetailSearchRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public OrderDetailResponse findOrderDetailByIdForCustomer(UUID orderId, User customerUser) {
    return null;
  }

  @Override
  public OrderDetailResponse findOrderDetailByIdForOwner(UUID orderId, User customerUser) {
    return null;
  }

  @Override
  public OrderDetailResponse findOrderDetailByIdForAdmin(UUID orderId) {
    return null;
  }
}
