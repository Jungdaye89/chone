package com.chone.server.domains.payment.dto.response;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.payment.domain.Payment;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreatePaymentResponse(
    UUID id,
    UUID orderId,
    int totalPrice,
    String status,
    String paymentMethod,
    LocalDateTime createdAt) {
  private CreatePaymentResponse(Payment payment, Order order) {
    this(
        payment.getId(),
        order.getId(),
        order.getTotalPrice().intValue(),
        payment.getStatus().getDescription(),
        payment.getPaymentMethod().getDescription(),
        payment.getCreatedAt());
  }

  public static CreatePaymentResponse from(Payment payment) {
    return new CreatePaymentResponse(payment, payment.getOrder());
  }
}
