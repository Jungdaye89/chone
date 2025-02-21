package com.chone.server.domains.payment.infrastructure.jpa;

import com.chone.server.domains.payment.domain.Payment;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {
  boolean existsByOrderIdAndDeletedAtNull(UUID orderId);

  Optional<Payment> findByIdAndDeletedAtNull(UUID orderId);

  Optional<Payment> findByOrderIdAndDeletedAtNull(UUID orderId);
}
