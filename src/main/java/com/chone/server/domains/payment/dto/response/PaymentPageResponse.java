package com.chone.server.domains.payment.dto.response;

import com.chone.server.domains.payment.domain.PaymentMethod;
import com.chone.server.domains.payment.domain.PaymentStatus;
import com.querydsl.core.annotations.QueryProjection;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentPageResponse(
    UUID id,
    UUID orderId,
    int totalPrice,
    String method,
    String status,
    LocalDateTime createdAt,
    LocalDateTime updatedAt) {

  @QueryProjection
  public PaymentPageResponse(
      UUID id,
      UUID orderId,
      int totalPrice,
      PaymentMethod method,
      PaymentStatus status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this(
        id,
        orderId,
        totalPrice,
        method.getDescription(),
        status.getDescription(),
        createdAt,
        updatedAt);
  }
}
