package com.chone.server.domains.payment.repository;

import com.chone.server.domains.payment.domain.Payment;
import java.util.UUID;

public interface PaymentRepository {

  boolean existsByOrderId(UUID id);

  Payment save(Payment payment);

  Payment findById(UUID id);

  Payment findByOrderId(UUID id);
}
