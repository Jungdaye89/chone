package com.chone.server.domains.payment.repository;

import java.util.UUID;

public interface PaymentRepository {

  boolean existsByOrderId(UUID id);
}
