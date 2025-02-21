package com.chone.server.domains.payment.infrastructure.jpa;

import static com.chone.server.domains.payment.domain.QPayment.payment;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.payment.domain.PaymentMethod;
import com.chone.server.domains.payment.domain.PaymentStatus;
import com.chone.server.domains.payment.dto.request.PaymentFilterParams;
import com.chone.server.domains.payment.dto.response.PaymentPageResponse;
import com.chone.server.domains.payment.dto.response.QPaymentPageResponse;
import com.chone.server.domains.payment.exception.PaymentExceptionCode;
import com.chone.server.domains.payment.repository.PaymentListSearchRepository;
import com.chone.server.domains.user.domain.User;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@RequiredArgsConstructor
public class PaymentListSearchRepositoryImpl implements PaymentListSearchRepository {
  private final JPAQueryFactory queryFactory;
  private final Map<String, Expression<?>> sortFieldMap =
      Map.of(
          "totalprice", payment.order.totalPrice,
          "createdat", payment.createdAt,
          "updatedat", payment.updatedAt);

  @Override
  public Page<PaymentPageResponse> findOrdersByCustomer(
      User customer, PaymentFilterParams filterParams, Pageable pageable) {
    validateNoCustomerFiltering(filterParams);

    List<BooleanExpression> conditions = new ArrayList<>();
    if (filterParams.storeId() != null) {
      addStoreIdCondition(conditions, filterParams.storeId());
    }
    conditions.add(customerEq(customer));

    return getPayments(conditions, filterParams, pageable);
  }

  private void validateNoCustomerFiltering(PaymentFilterParams filterParams) {
    if (filterParams.customerId() != null) {
      throw new ApiBusinessException(PaymentExceptionCode.CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED);
    }
  }

  private BooleanExpression customerEq(User customer) {
    return payment.order.user.eq(customer);
  }

  @Override
  public Page<PaymentPageResponse> findOrdersByOwner(
      User owner, PaymentFilterParams filterParams, Pageable pageable) {
    validateNoStoreFiltering(filterParams.storeId());

    List<BooleanExpression> conditions = new ArrayList<>();
    if (filterParams.customerId() != null) {
      addCustomerIdCondition(conditions, filterParams.customerId());
    }
    conditions.add(storeOwnerEq(owner));

    return getPayments(conditions, filterParams, pageable);
  }

  private void validateNoStoreFiltering(UUID storeId) {
    if (storeId != null) {
      throw new ApiBusinessException(PaymentExceptionCode.STORE_PAYMENT_FILTERING_ACCESS_DENIED);
    }
  }

  private BooleanExpression storeOwnerEq(User owner) {
    return payment.order.user.eq(owner);
  }

  @Override
  public Page<PaymentPageResponse> findOrdersByAdmin(
      User admin, PaymentFilterParams filterParams, Pageable pageable) {
    List<BooleanExpression> conditions = new ArrayList<>();
    if (filterParams.storeId() != null) {
      addStoreIdCondition(conditions, filterParams.storeId());
    }
    if (filterParams.customerId() != null) {
      addCustomerIdCondition(conditions, filterParams.customerId());
    }
    return getPayments(conditions, filterParams, pageable);
  }

  private void addStoreIdCondition(List<BooleanExpression> conditions, UUID storeId) {
    if (storeId != null) {
      conditions.add(payment.order.store.id.eq(storeId));
    }
  }

  private void addCustomerIdCondition(List<BooleanExpression> conditions, Long customerId) {
    if (customerId != null) {
      conditions.add(payment.order.user.id.eq(customerId));
    }
  }

  private Page<PaymentPageResponse> getPayments(
      List<BooleanExpression> conditions, PaymentFilterParams filterParams, Pageable pageable) {

    conditions.addAll(
        Stream.of(
                payment.deletedAt.isNull(),
                statusEq(filterParams.status()),
                paymentMethodEq(filterParams.method()),
                totalPriceBetween(filterParams.minPrice(), filterParams.maxPrice()),
                createdAtBetween(filterParams.startDate(), filterParams.endDate()))
            .filter(Objects::nonNull)
            .toList());

    BooleanExpression whereCondition =
        conditions.stream().reduce(BooleanExpression::and).orElse(null);

    List<PaymentPageResponse> content =
        queryFactory
            .select(
                new QPaymentPageResponse(
                    payment.id,
                    payment.order.id,
                    payment.order.totalPrice,
                    payment.paymentMethod,
                    payment.status,
                    payment.createdAt,
                    payment.updatedAt))
            .from(payment)
            .join(payment.order)
            .where(whereCondition)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifier(pageable))
            .fetch();

    JPAQuery<Long> countQuery =
        queryFactory
            .select(payment.count())
            .from(payment)
            .join(payment.order)
            .where(whereCondition);

    return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
  }

  private BooleanExpression statusEq(PaymentStatus status) {
    return status != null ? payment.status.eq(status) : null;
  }

  private BooleanExpression paymentMethodEq(PaymentMethod method) {
    return method != null ? payment.paymentMethod.eq(method) : null;
  }

  private BooleanExpression totalPriceBetween(Integer minPrice, Integer maxPrice) {
    if (minPrice != null && maxPrice != null) {
      return payment.order.totalPrice.between(minPrice, maxPrice);
    } else if (minPrice != null) {
      return payment.order.totalPrice.goe(minPrice);
    } else if (maxPrice != null) {
      return payment.order.totalPrice.loe(maxPrice);
    }
    return null;
  }

  private BooleanExpression createdAtBetween(LocalDate startDate, LocalDate endDate) {
    try {
      if (startDate != null && endDate != null) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);
        return payment.createdAt.between(startDateTime, endDateTime);
      } else if (startDate != null) {
        LocalDateTime startDateTime = startDate.atStartOfDay();
        return payment.createdAt.goe(startDateTime);
      } else if (endDate != null) {
        LocalDateTime endDateTime = endDate.atTime(23, 59, 59, 999999999);
        return payment.createdAt.loe(endDateTime);
      }
    } catch (Exception e) {
      log.warn("날짜 필터링 형성 실패: {}", e.getMessage());
    }
    return null;
  }

  private OrderSpecifier[] getOrderSpecifier(Pageable pageable) {
    if (!pageable.getSort().isSorted()) {
      return new OrderSpecifier[] {new OrderSpecifier(Order.DESC, payment.createdAt)};
    }

    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();
    for (Sort.Order sortOrder : pageable.getSort()) {
      Order direction = sortOrder.getDirection().isAscending() ? Order.ASC : Order.DESC;
      Expression<?> sortField =
          sortFieldMap.getOrDefault(sortOrder.getProperty().toLowerCase(), payment.createdAt);
      orderSpecifiers.add(new OrderSpecifier(direction, sortField));
    }

    return orderSpecifiers.isEmpty()
        ? new OrderSpecifier[] {new OrderSpecifier(Order.DESC, payment.createdAt)}
        : orderSpecifiers.toArray(new OrderSpecifier[0]);
  }
}
