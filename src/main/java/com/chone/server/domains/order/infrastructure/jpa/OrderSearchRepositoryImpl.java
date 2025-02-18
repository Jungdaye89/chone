package com.chone.server.domains.order.infrastructure.jpa;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.dto.request.OrderFilterParams;
import com.chone.server.domains.order.dto.response.OrderPageResponse;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderSearchRepository;
import com.chone.server.domains.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderSearchRepositoryImpl implements OrderSearchRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public Page<OrderPageResponse> findOrdersByCustomer(
      User customer, OrderFilterParams filterParams, Pageable pageable) {
    validateNoUnauthorizedFiltering(filterParams);
    List<BooleanExpression> conditions = new ArrayList<>();
    addCustomerCondition(conditions, customer);
    return getOrders(conditions, filterParams, pageable);
  }

  @Override
  public Page<OrderPageResponse> findOrdersByOwner(
      User owner, OrderFilterParams filterParams, Pageable pageable) {
    return null;
  }

  @Override
  public Page<OrderPageResponse> findOrdersByAdmin(
      User admin, OrderFilterParams filterParams, Pageable pageable) {
    return null;
  }

  private void validateNoUnauthorizedFiltering(OrderFilterParams filterParams) {
    if (filterParams.storeId() != null || filterParams.customerId() != null) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_FILTERING_ACCESS_DENIED);
    }
  }

  private void addCustomerCondition(List<BooleanExpression> conditions, User customer) {}

  private Page<OrderPageResponse> getOrders(
      List<BooleanExpression> conditions, OrderFilterParams filterParams, Pageable pageable) {
    return null;
  }
}
