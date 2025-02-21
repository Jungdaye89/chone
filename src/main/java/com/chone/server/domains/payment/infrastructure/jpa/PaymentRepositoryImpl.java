package com.chone.server.domains.payment.infrastructure.jpa;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.dto.request.PaymentFilterParams;
import com.chone.server.domains.payment.dto.response.PaymentPageResponse;
import com.chone.server.domains.payment.exception.PaymentExceptionCode;
import com.chone.server.domains.payment.repository.PaymentRepository;
import com.chone.server.domains.user.domain.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
  private final PaymentJpaRepository jpaRepository;

  @Override
  public boolean existsByOrderId(UUID id) {
    return jpaRepository.existsByOrderIdAndDeletedAtNull(id);
  }

  @Override
  public Payment save(Payment payment) {
    return jpaRepository.save(payment);
  }

  @Override
  public Payment findById(UUID id) {
    return jpaRepository
        .findByIdAndDeletedAtNull(id)
        .orElseThrow(() -> new ApiBusinessException(PaymentExceptionCode.NOT_FOUND_PAYMENT));
  }

  @Override
  public Payment findByOrderId(UUID orderId) {
    return jpaRepository
        .findByOrderIdAndDeletedAtNull(orderId)
        .orElseThrow(() -> new ApiBusinessException(PaymentExceptionCode.NOT_FOUND_PAYMENT));
  }

  @Override
  public Page<PaymentPageResponse> findPaymentsByCustomer(
      User user, PaymentFilterParams filterParams, Pageable pageable) {
    return null;
  }

  @Override
  public Page<PaymentPageResponse> findPaymentsByOwner(
      User user, PaymentFilterParams filterParams, Pageable pageable) {
    return null;
  }

  @Override
  public Page<PaymentPageResponse> findPaymentsByAdmin(
      User user, PaymentFilterParams filterParams, Pageable pageable) {
    return null;
  }
}
