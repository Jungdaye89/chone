package com.chone.server.domains.payment.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.service.OrderService;
import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.payment.dto.request.CreatePaymentRequest;
import com.chone.server.domains.payment.dto.response.CreatePaymentResponse;
import com.chone.server.domains.payment.exception.PaymentExceptionCode;
import com.chone.server.domains.payment.repository.PaymentRepository;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService {
  private final PaymentRepository repository;

  private final PaymentDomainService domainService;
  private final OrderService orderService;

  @Transactional
  public CreatePaymentResponse processPayment(
      @Valid CreatePaymentRequest requestDto, CustomUserDetails principal) {
    Order order = orderService.findByOrderId(requestDto.orderId());

    verifyNoExistingPayment(order.getId());
    domainService.validatePaymentRequest(order, requestDto, principal.getUser());

    Payment payment = null;
    return CreatePaymentResponse.from(payment);
  }

  private void verifyNoExistingPayment(UUID orderId) {
    if (repository.existsByOrderId(orderId)) {
      throw new ApiBusinessException(PaymentExceptionCode.ALREADY_PAID);
    }
  }
}
