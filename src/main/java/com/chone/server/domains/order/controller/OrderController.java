package com.chone.server.domains.order.controller;

import com.chone.server.commons.util.UriGeneratorUtil;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.dto.request.CreateOrderRequest;
import com.chone.server.domains.order.dto.response.CreateOrderResponse;
import com.chone.server.domains.order.service.OrderService;
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
@RequestMapping("/api/v1/orders")
public class OrderController {
  private final OrderService service;

  @PreAuthorize("hasAnyRole('CUSTOMER', 'STORE_OWNER')")
  @PostMapping
  public ResponseEntity<CreateOrderResponse> createOrder(
      @Valid @RequestBody CreateOrderRequest requestDto,
      @AuthenticationPrincipal CustomUserDetails principal) {
    CreateOrderResponse responseDto = service.createOrder(requestDto, principal);

    return ResponseEntity.created(UriGeneratorUtil.generateUri("")).body(responseDto);
  }
}
