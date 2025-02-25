package com.chone.server.domains.payment.infrastructure.jpa;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.domain.PgPaymentLog;
import com.chone.server.domains.payment.exception.PgPaymentLogExceptionCode;
import com.chone.server.domains.payment.repository.PgPaymentLogRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PgPaymentLogRepositoryImpl implements PgPaymentLogRepository {
  private final PgPaymentLogJpaRepository jpaRepository;

  @Override
  public PgPaymentLog findById(UUID id) {
    return jpaRepository
        .findByIdAndDeletedAtNull(id)
        .orElseThrow(
            () -> new ApiBusinessException(PgPaymentLogExceptionCode.NOT_FOUND_PG_PAYMENT_LOG));
  }

  @Override
  public PgPaymentLog findByPayment(Payment payment) {
    return jpaRepository
        .findByPaymentAndDeletedAtNull(payment)
        .orElseThrow(
            () -> new ApiBusinessException(PgPaymentLogExceptionCode.NOT_FOUND_PG_PAYMENT_LOG));
  }

  @Override
  public PgPaymentLog save(PgPaymentLog pgPaymentLog) {
    return jpaRepository.save(pgPaymentLog);
  }
}
