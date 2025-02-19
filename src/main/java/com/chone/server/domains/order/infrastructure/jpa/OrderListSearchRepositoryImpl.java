package com.chone.server.domains.order.infrastructure.jpa;

import static com.chone.server.domains.order.domain.QOrder.order;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.order.dto.request.OrderFilterParams;
import com.chone.server.domains.order.dto.response.OrderPageResponse;
import com.chone.server.domains.order.dto.response.QOrderPageResponse;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderListSearchRepository;
import com.chone.server.domains.user.domain.User;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderListSearchRepositoryImpl implements OrderListSearchRepository {
  private final JPAQueryFactory queryFactory;
  private final Map<String, Expression<?>> sortFieldMap =
      Map.of(
          "totalprice", order.totalPrice,
          "createdat", order.createdAt,
          "updatedat", order.updatedAt);

  @Override
  public Page<OrderPageResponse> findOrdersByCustomer(
      User customer, OrderFilterParams filterParams, Pageable pageable) {
    validateNoUnauthorizedFiltering(filterParams);

    return getOrders(List.of(customerEq(customer)), filterParams, pageable);
  }

  @Override
  public Page<OrderPageResponse> findOrdersByOwner(
      User owner, OrderFilterParams filterParams, Pageable pageable) {
    validateNoStoreFiltering(filterParams.storeId());

    return getOrders(List.of(storeOwnerEq(owner)), filterParams, pageable);
  }

  @Override
  public Page<OrderPageResponse> findOrdersByAdmin(
      User admin, OrderFilterParams filterParams, Pageable pageable) {
    List<BooleanExpression> conditions = new ArrayList<>();
    if (filterParams.storeId() != null) {
      addStoreIdCondition(conditions, filterParams.storeId());
    }
    if (filterParams.customerId() != null) {
      addCustomerIdCondition(conditions, filterParams.customerId());
    }
    return getOrders(conditions, filterParams, pageable);
  }

  private void validateNoUnauthorizedFiltering(OrderFilterParams filterParams) {
    if (filterParams.storeId() != null || filterParams.customerId() != null) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_FILTERING_ACCESS_DENIED);
    }
  }

  private void validateNoStoreFiltering(UUID storeId) {
    if (storeId != null) {
      throw new ApiBusinessException(OrderExceptionCode.STORE_ORDER_FILTERING_ACCESS_DENIED);
    }
  }

  private BooleanExpression customerEq(User customer) {
    return order.user.eq(customer);
  }

  private BooleanExpression storeOwnerEq(User owner) {
    return order.store.user.eq(owner);
  }

  private void addStoreIdCondition(List<BooleanExpression> conditions, UUID storeId) {
    if (storeId != null) {
      conditions.add(order.store.id.eq(storeId));
    }
  }

  private void addCustomerIdCondition(List<BooleanExpression> conditions, Long customerId) {
    if (customerId != null) {
      conditions.add(order.user.id.eq(customerId));
    }
  }

  private Page<OrderPageResponse> getOrders(
      List<BooleanExpression> additionalConditions,
      OrderFilterParams filterParams,
      Pageable pageable) {

    List<BooleanExpression> conditions = new ArrayList<>(additionalConditions);
    conditions.addAll(
        Stream.of(
                order.deletedAt.isNull(),
                statusEq(filterParams.status()),
                orderTypeEq(filterParams.orderType()),
                totalPriceBetween(filterParams.minPrice(), filterParams.maxPrice()),
                createdAtBetween(filterParams.startDate(), filterParams.endDate()))
            .filter(Objects::nonNull)
            .toList());

    BooleanExpression whereCondition =
        conditions.stream().reduce(BooleanExpression::and).orElse(null);

    List<OrderPageResponse> content =
        queryFactory
            .select(
                new QOrderPageResponse(
                    order.id,
                    order.store.id,
                    order.store.name,
                    order.orderType,
                    order.status,
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

  private BooleanExpression statusEq(OrderStatus status) {
    return status != null ? order.status.eq(status) : null;
  }

  private BooleanExpression orderTypeEq(OrderType orderType) {
    return orderType != null ? order.orderType.eq(orderType) : null;
  }

  private BooleanExpression totalPriceBetween(Integer minPrice, Integer maxPrice) {
    try {
      if (minPrice != null && maxPrice != null) {
        return order.totalPrice.between(BigDecimal.valueOf(minPrice), BigDecimal.valueOf(maxPrice));
      } else if (minPrice != null) {
        return order.totalPrice.goe(BigDecimal.valueOf(minPrice));
      } else if (maxPrice != null) {
        return order.totalPrice.loe(BigDecimal.valueOf(maxPrice));
      }
    } catch (NumberFormatException e) {
      log.warn("가격 형변환 실패: {}", e.getMessage());
    } catch (Exception e) {
      log.warn("가격 필터링 조건 형성 실패: {}", e.getMessage());
    }
    return null;
  }

  private BooleanExpression createdAtBetween(LocalDate startDate, LocalDate endDate) {
    try {
      if (startDate != null && endDate != null) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);
        return order.createdAt.between(startDateTime, endDateTime);
      } else if (startDate != null) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        return order.createdAt.goe(startDateTime);
      } else if (endDate != null) {
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);
        return order.createdAt.loe(endDateTime);
      }
    } catch (Exception e) {
      log.warn("날짜 필터링 형성 실패: {}", e.getMessage());
    }
    return null;
  }

  private OrderSpecifier[] getOrderSpecifier(Pageable pageable) {
    if (!pageable.getSort().isSorted()) {
      return new OrderSpecifier[] {new OrderSpecifier(Order.DESC, order.createdAt)};
    }

    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    for (Sort.Order sortOrder : pageable.getSort()) {
      Order direction = sortOrder.getDirection().isAscending() ? Order.ASC : Order.DESC;
      Expression<?> sortField =
          sortFieldMap.getOrDefault(sortOrder.getProperty().toLowerCase(), order.createdAt);
      orderSpecifiers.add(new OrderSpecifier(direction, sortField));
    }

    return orderSpecifiers.isEmpty()
        ? new OrderSpecifier[] {new OrderSpecifier(Order.DESC, order.createdAt)}
        : orderSpecifiers.toArray(new OrderSpecifier[0]);
  }
}
