package com.chone.server.domains.payment.controller;

import com.chone.server.commons.util.UriGeneratorUtil;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.payment.dto.request.CreatePaymentRequest;
import com.chone.server.domains.payment.dto.response.CreatePaymentResponse;
import com.chone.server.domains.payment.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
  private final PaymentService service;

  @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
  @PostMapping
  public ResponseEntity<CreatePaymentResponse> createOrder(
      @Valid @RequestBody CreatePaymentRequest requestDto,
      @AuthenticationPrincipal CustomUserDetails principal) {
    CreatePaymentResponse responseDto = service.processPayment(requestDto, principal);

    return ResponseEntity.created(UriGeneratorUtil.generateUri("/" + responseDto.id().toString()))
        .body(responseDto);
  }
}
