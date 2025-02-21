package com.chone.server.domains.payment.infrastructure.jpa;

import com.chone.server.domains.payment.domain.PgPaymentLog;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PgPaymentLogJpaRepository extends JpaRepository<PgPaymentLog, UUID> {
  Optional<PgPaymentLog> findByIdAndDeletedAtNull(UUID id);
}
