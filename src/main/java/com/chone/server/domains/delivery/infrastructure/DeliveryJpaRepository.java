package com.chone.server.domains.delivery.infrastructure;

import com.chone.server.domains.order.domain.Delivery;
import com.chone.server.domains.order.domain.Order;
import jakarta.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID> {
  Optional<Delivery> findByIdAndDeletedAtNull(UUID id);

  Optional<Delivery> findByOrderAndDeletedAtNull(Order order);

  @Nullable
  @Query("SELECT d FROM Delivery d WHERE d.order.id = :orderId AND d.deletedAt IS NULL")
  Delivery findByOrderIdOrNull(@Param("orderId") UUID orderId);
}
