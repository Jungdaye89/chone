package com.chone.server.domains.payment.service;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.payment.dto.request.CreatePaymentRequest;
import com.chone.server.domains.payment.dto.response.CreatePaymentResponse;
import com.chone.server.domains.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class PaymentService {
  private final PaymentRepository repository;

  public CreatePaymentResponse processPayment(
      CreatePaymentRequest requestDto, CustomUserDetails principal) {
    return null;
  }
}
