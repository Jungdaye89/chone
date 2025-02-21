package com.chone.server.domains.payment.service;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.dto.response.PageResponse;
import com.chone.server.domains.payment.dto.request.PaymentFilterParams;
import com.chone.server.domains.payment.dto.response.PaymentDetailResponse;
import com.chone.server.domains.payment.dto.response.PaymentPageResponse;
import com.chone.server.domains.payment.repository.PaymentRepository;
import com.chone.server.domains.user.domain.User;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional(readOnly = true)
public class PaymentReadService {

  private final PaymentRepository repository;
  private final PaymentDomainService domainService;

  public PageResponse<PaymentPageResponse> getPayments(
      CustomUserDetails principal, PaymentFilterParams filterParams, Pageable pageable) {
    User user = principal.getUser();
    return PageResponse.from(findPaymentsByRole(user, filterParams, pageable));
  }

  public PaymentDetailResponse getPaymentById(CustomUserDetails principal, UUID id) {
    User user = principal.getUser();
    PaymentDetailResponse paymentResponse = repository.findPaymentWithDetails(id).toResponse();

    switch (user.getRole()) {
      case CUSTOMER -> domainService.validateCustomerViewPermission(paymentResponse, user);
      case OWNER -> domainService.validateOwnerViewPermission(paymentResponse, user);
      case MANAGER, MASTER -> {}
    }

    return paymentResponse;
  }

  private Page<PaymentPageResponse> findPaymentsByRole(
      User user, PaymentFilterParams filterParams, Pageable pageable) {
    return switch (user.getRole()) {
      case CUSTOMER -> repository.findPaymentsByCustomer(user, filterParams, pageable);
      case OWNER -> repository.findPaymentsByOwner(user, filterParams, pageable);
      case MANAGER, MASTER -> repository.findPaymentsByAdmin(user, filterParams, pageable);
    };
  }
}
