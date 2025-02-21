package com.chone.server.domains.payment.dto.response;

import java.util.UUID;

public record PaymentDetailResponse(PaymentResponse payment, OrderResponse order) {
  public record PaymentResponse(UUID id, String status, String cancelReason, String method) {}

  public record OrderResponse(
      UUID id,
      int totalPrice,
      String type,
      Long userId,
      UUID storeId,
      String storeName,
      Long storeUserId) {}
}
