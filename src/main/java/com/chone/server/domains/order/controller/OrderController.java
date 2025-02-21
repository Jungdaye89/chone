package com.chone.server.domains.order.controller;

import com.chone.server.commons.util.UriGeneratorUtil;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.dto.request.CancelOrderRequest;
import com.chone.server.domains.order.dto.request.CreateOrderRequest;
import com.chone.server.domains.order.dto.request.OrderFilterParams;
import com.chone.server.domains.order.dto.request.OrderStatusUpdateRequest;
import com.chone.server.domains.order.dto.response.CancelOrderResponse;
import com.chone.server.domains.order.dto.response.CreateOrderResponse;
import com.chone.server.domains.order.dto.response.DeleteOrderResponse;
import com.chone.server.domains.order.dto.response.OrderDetailResponse;
import com.chone.server.domains.order.dto.response.OrderPageResponse;
import com.chone.server.domains.order.dto.response.OrderStatusUpdateResponse;
import com.chone.server.domains.order.dto.response.PageResponse;
import com.chone.server.domains.order.service.OrderCancellationService;
import com.chone.server.domains.order.service.OrderCreationService;
import com.chone.server.domains.order.service.OrderDeletionService;
import com.chone.server.domains.order.service.OrderReadService;
import com.chone.server.domains.order.service.OrderUpdateService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
  private final OrderCreationService creationService;
  private final OrderReadService readService;
  private final OrderCancellationService cancellationService;
  private final OrderDeletionService deletionService;
  private final OrderUpdateService updateService;

  @PreAuthorize("hasAnyRole('CUSTOMER', 'OWNER')")
  @PostMapping
  public ResponseEntity<CreateOrderResponse> createOrder(
      @Valid @RequestBody CreateOrderRequest requestDto,
      @AuthenticationPrincipal CustomUserDetails principal) {
    CreateOrderResponse responseDto = creationService.createOrder(requestDto, principal);

    return ResponseEntity.created(UriGeneratorUtil.generateUri("/" + responseDto.id().toString()))
        .body(responseDto);
  }

  @GetMapping
  public ResponseEntity<PageResponse<OrderPageResponse>> getOrders(
      @AuthenticationPrincipal CustomUserDetails principal,
      @ModelAttribute("filterParams") OrderFilterParams filterParams,
      @PageableDefault(page = 0, size = 10, sort = "createdat", direction = Sort.Direction.DESC)
          Pageable pageable) {
    PageResponse<OrderPageResponse> responseDto =
        readService.getOrders(principal, filterParams, pageable);

    return ResponseEntity.ok().body(responseDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderDetailResponse> getOrder(
      @AuthenticationPrincipal CustomUserDetails principal, @PathVariable("id") UUID id) {
    OrderDetailResponse responseDto = readService.getOrderById(principal, id);

    return ResponseEntity.ok().body(responseDto);
  }

  @PatchMapping("/{id}")
  public ResponseEntity<CancelOrderResponse> cancelOrder(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("id") UUID id,
      @Valid @RequestBody CancelOrderRequest requestDto) {
    CancelOrderResponse responseDto = cancellationService.cancelOrder(principal, id, requestDto);

    return ResponseEntity.ok().body(responseDto);
  }

  @PreAuthorize("hasAnyRole('MANAGER','MASTER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<DeleteOrderResponse> deleteOrder(
      @AuthenticationPrincipal CustomUserDetails principal, @PathVariable("id") UUID id) {
    DeleteOrderResponse responseDto = deletionService.deleteOrder(principal, id);

    return ResponseEntity.ok().body(responseDto);
  }

  @PreAuthorize("!hasAnyRole('CUSTOMER')")
  @PatchMapping("/{id}/status")
  public ResponseEntity<OrderStatusUpdateResponse> updateOrderStatus(
      @AuthenticationPrincipal CustomUserDetails principal,
      @PathVariable("id") UUID id,
      @RequestBody OrderStatusUpdateRequest request) {
    OrderStatusUpdateResponse response = updateService.updateOrderStatus(principal, id, request);
    return ResponseEntity.ok(response);
  }
}
