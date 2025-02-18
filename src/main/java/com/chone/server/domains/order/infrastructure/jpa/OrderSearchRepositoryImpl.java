package com.chone.server.domains.order.infrastructure.jpa;

import static com.chone.server.domains.order.domain.QOrder.order;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.order.dto.request.OrderFilterParams;
import com.chone.server.domains.order.dto.response.OrderPageResponse;
import com.chone.server.domains.order.dto.response.QOrderPageResponse;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderSearchRepository;
import com.chone.server.domains.user.domain.User;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
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

  private void addCustomerCondition(List<BooleanExpression> conditions, User customer) {
    if (customer != null) {
      conditions.add(order.user.eq(customer));
    }
  }

  private Page<OrderPageResponse> getOrders(
      List<BooleanExpression> conditions, OrderFilterParams filterParams, Pageable pageable) {

    addStatusCondition(conditions, filterParams.status());
    addOrderTypeCondition(conditions, filterParams.orderType());
    addPriceCondition(conditions, filterParams.minPrice(), filterParams.maxPrice());
    addDateCondition(conditions, filterParams.startDate(), filterParams.endDate());

    BooleanExpression whereCondition = buildWhereCondition(conditions);

    List<OrderPageResponse> content =
        queryFactory
            .select(
                new QOrderPageResponse(
                    order.id,
                    order.store.id,
                    order.store.name,
                    order.orderType.stringValue(),
                    order.status.stringValue(),
                    order.totalPrice,
                    order.createdAt,
                    order.updatedAt))
            .from(order)
            .join(order.store)
            .where(whereCondition)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifier(pageable))
            .fetch();

    JPAQuery<Long> countQuery =
        queryFactory.select(order.count()).from(order).join(order.store).where(whereCondition);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  private void addStatusCondition(List<BooleanExpression> conditions, OrderStatus status) {}

  private void addOrderTypeCondition(List<BooleanExpression> conditions, OrderType orderType) {}

  private void addPriceCondition(
      List<BooleanExpression> conditions, Integer integer, Integer integer1) {}

  private void addDateCondition(
      List<BooleanExpression> conditions, LocalDate localDate, LocalDate localDate1) {}

  private BooleanExpression buildWhereCondition(List<BooleanExpression> conditions) {
    return null;
  }

  private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
    return null;
  }
}
