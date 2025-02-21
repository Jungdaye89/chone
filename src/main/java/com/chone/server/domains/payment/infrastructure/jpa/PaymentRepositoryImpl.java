package com.chone.server.domains.payment.infrastructure.jpa;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.exception.PaymentExceptionCode;
import com.chone.server.domains.payment.repository.PaymentRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
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
}
