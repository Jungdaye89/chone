package com.chone.server.domains.payment.infrastructure.jpa;

import com.chone.server.domains.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {
  private final PaymentJpaRepository jpaRepository;
}
