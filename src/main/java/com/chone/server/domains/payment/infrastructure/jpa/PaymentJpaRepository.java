package com.chone.server.domains.payment.infrastructure.jpa;

import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.repository.dto.PaymentDetailDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PaymentJpaRepository extends JpaRepository<Payment, UUID> {
  boolean existsByOrderIdAndDeletedAtNull(UUID orderId);

  Optional<Payment> findByIdAndDeletedAtNull(UUID orderId);

  Optional<Payment> findByOrderIdAndDeletedAtNull(UUID orderId);

  @Query(
      """
    SELECT new com.chone.server.domains.payment.repository.dto.PaymentDetailDto(
        p.id, p.status, p.cancelReason, p.paymentMethod,
        o.id, o.totalPrice, o.orderType, o.store.user.id,
        u.id, s.id, s.name
    )
    FROM Payment p
    JOIN p.order o
    JOIN o.user u
    JOIN o.store s
    WHERE p.id = :id
    AND p.deletedAt IS NULL
    """)
  Optional<PaymentDetailDto> findPaymentDetailById(@Param("id") UUID id);
}
