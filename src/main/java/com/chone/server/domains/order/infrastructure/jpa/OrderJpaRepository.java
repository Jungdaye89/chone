package com.chone.server.domains.order.infrastructure.jpa;

import com.chone.server.domains.order.domain.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {}
