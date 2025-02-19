package com.chone.server.domains.order.infrastructure.jpa;

import com.chone.server.domains.order.domain.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {
  @Query(
      "SELECT o FROM Order o "
          + "JOIN FETCH o.user "
          + "JOIN FETCH o.store s "
          + "JOIN FETCH s.user "
          + "WHERE o.id = :orderId AND (o.deletedAt IS NULL)")
  Optional<Order> findForCancellationById(@Param("orderId") UUID orderId);
}
