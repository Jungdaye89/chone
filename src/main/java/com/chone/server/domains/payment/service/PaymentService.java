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
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService {
  private final PaymentRepository repository;

  private final OrderService orderService;

  @Transactional
  public CreatePaymentResponse processPayment(
      @Valid CreatePaymentRequest requestDto, CustomUserDetails principal) {
    Order order = orderService.findByOrderId(requestDto.orderId());

    boolean paymentExists = repository.existsByOrderId(order.getId());
    if (paymentExists) {
      throw new ApiBusinessException(PaymentExceptionCode.CONFLICT_ALREADY_PAID);
    }

    Payment payment = null;
    return CreatePaymentResponse.from(payment);
  }
}
