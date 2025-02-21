package com.chone.server.domains.payment.infrastructure.jpa;

import static com.chone.server.domains.payment.domain.QPayment.payment;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.payment.dto.request.PaymentFilterParams;
import com.chone.server.domains.payment.dto.response.PaymentPageResponse;
import com.chone.server.domains.payment.exception.PaymentExceptionCode;
import com.chone.server.domains.payment.repository.PaymentListSearchRepository;
import com.chone.server.domains.user.domain.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Log4j2
@Repository
@RequiredArgsConstructor
public class PaymentListSearchRepositoryImpl implements PaymentListSearchRepository {

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
      List<BooleanExpression> additionalConditions,
      PaymentFilterParams filterParams,
      Pageable pageable) {
    return null;
  }
}
