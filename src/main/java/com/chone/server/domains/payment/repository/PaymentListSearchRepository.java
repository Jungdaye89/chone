package com.chone.server.domains.payment.repository;

import com.chone.server.domains.payment.dto.request.PaymentFilterParams;
import com.chone.server.domains.payment.dto.response.PaymentPageResponse;
import com.chone.server.domains.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentListSearchRepository {
  Page<PaymentPageResponse> findOrdersByCustomer(
      User customer, PaymentFilterParams filterParams, Pageable pageable);

  Page<PaymentPageResponse> findOrdersByOwner(
      User owner, PaymentFilterParams filterParams, Pageable pageable);

  Page<PaymentPageResponse> findOrdersByAdmin(
      User admin, PaymentFilterParams filterParams, Pageable pageable);
}
