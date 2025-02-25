package com.chone.server.domains.payment.dto.response;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.payment.domain.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "결제 생성 응답")
public record CreatePaymentResponse(
    @Schema(
            description = "결제 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        UUID id,
    @Schema(
            description = "주문 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        UUID orderId,
    @Schema(description = "총 결제 금액", example = "15000", required = true) int totalPrice,
    @Schema(description = "결제 상태", example = "결제 완료", required = true) String status,
    @Schema(description = "결제 수단", example = "카드", required = true) String paymentMethod,
    @Schema(description = "결제 생성 시간", required = true) LocalDateTime createdAt) {
  private CreatePaymentResponse(Payment payment, Order order) {
    this(
        payment.getId(),
        order.getId(),
        order.getTotalPrice(),
        payment.getStatus().getDescription(),
        payment.getPaymentMethod().getDescription(),
        payment.getCreatedAt());
  }

  public static CreatePaymentResponse from(Payment payment) {
    return new CreatePaymentResponse(payment, payment.getOrder());
  }
}
