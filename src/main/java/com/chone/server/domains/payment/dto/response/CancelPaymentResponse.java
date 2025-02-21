package com.chone.server.domains.payment.dto.response;

import com.chone.server.domains.payment.domain.Payment;
import java.time.LocalDateTime;

public record CancelPaymentResponse(
    boolean isSuccess, String status, LocalDateTime cancelRequestedAt) {
  private CancelPaymentResponse(boolean isSuccess, Payment payment) {
    this(isSuccess, payment.getStatus().getDescription(), payment.getUpdatedAt());
  }

  public static CancelPaymentResponse from(boolean isSuccess, Payment payment) {
    return new CancelPaymentResponse(isSuccess, payment);
  }
}
