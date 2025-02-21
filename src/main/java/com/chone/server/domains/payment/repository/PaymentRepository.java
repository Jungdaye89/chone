package com.chone.server.domains.payment.repository;

import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.dto.request.PaymentFilterParams;
import com.chone.server.domains.payment.dto.response.PaymentPageResponse;
import com.chone.server.domains.user.domain.User;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentRepository {

  boolean existsByOrderId(UUID id);

  Payment save(Payment payment);

  Payment findById(UUID id);

  Payment findByOrderId(UUID id);

  Page<PaymentPageResponse> findPaymentsByCustomer(
      User user, PaymentFilterParams filterParams, Pageable pageable);

  Page<PaymentPageResponse> findPaymentsByOwner(
      User user, PaymentFilterParams filterParams, Pageable pageable);

  Page<PaymentPageResponse> findPaymentsByAdmin(
      User user, PaymentFilterParams filterParams, Pageable pageable);
}
