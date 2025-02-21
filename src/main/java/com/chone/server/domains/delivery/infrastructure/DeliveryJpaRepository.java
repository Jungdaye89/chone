package com.chone.server.domains.delivery.infrastructure;

import com.chone.server.domains.order.domain.Delivery;
import com.chone.server.domains.order.domain.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID> {
  Optional<Delivery> findByIdAndDeletedAtNull(UUID id);

  Optional<Delivery> findByOrderAndDeletedAtNull(Order order);
}
