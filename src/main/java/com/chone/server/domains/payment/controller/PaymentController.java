package com.chone.server.domains.payment.controller;

import com.chone.server.commons.util.UriGeneratorUtil;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.dto.response.PageResponse;
import com.chone.server.domains.payment.dto.request.CreatePaymentRequest;
import com.chone.server.domains.payment.dto.request.PaymentFilterParams;
import com.chone.server.domains.payment.dto.response.CreatePaymentResponse;
import com.chone.server.domains.payment.dto.response.PaymentDetailResponse;
import com.chone.server.domains.payment.dto.response.PaymentPageResponse;
import com.chone.server.domains.payment.service.PaymentProcessService;
import com.chone.server.domains.payment.service.PaymentReadService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/payments")
public class PaymentController {
  private final PaymentProcessService processService;
  private final PaymentReadService readService;

  @PostMapping
  public ResponseEntity<CreatePaymentResponse> createOrder(
      @Valid @RequestBody CreatePaymentRequest requestDto,
      @AuthenticationPrincipal CustomUserDetails principal) {
    CreatePaymentResponse responseDto = processService.processPayment(requestDto, principal);

    return ResponseEntity.created(UriGeneratorUtil.generateUri("/" + responseDto.id().toString()))
        .body(responseDto);
  }

  @GetMapping
  public ResponseEntity<PageResponse<PaymentPageResponse>> getPayments(
      @AuthenticationPrincipal CustomUserDetails principal,
      @ModelAttribute("filterParams") PaymentFilterParams filterParams,
      @PageableDefault(page = 0, size = 10, sort = "createdat", direction = Sort.Direction.DESC)
          Pageable pageable) {
    PageResponse<PaymentPageResponse> responseDto =
        readService.getPayments(principal, filterParams, pageable);

    return ResponseEntity.ok().body(responseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<PaymentDetailResponse> getPayment(
      @AuthenticationPrincipal CustomUserDetails principal, @PathVariable("id") UUID id) {
    PaymentDetailResponse responseDto = readService.getPaymentById(principal, id);

    return ResponseEntity.ok().body(responseDto);
  }
}
