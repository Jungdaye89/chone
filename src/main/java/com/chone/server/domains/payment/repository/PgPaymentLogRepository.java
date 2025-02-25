package com.chone.server.domains.payment.repository;

import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.domain.PgPaymentLog;
import java.util.UUID;

public interface PgPaymentLogRepository {
  PgPaymentLog findById(UUID id);

  PgPaymentLog findByPayment(Payment payment);

  PgPaymentLog save(PgPaymentLog pgPaymentLog);
}
