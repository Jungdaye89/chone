package com.chone.server.domains.payment.dto.response;

import com.chone.server.domains.payment.domain.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "결제 취소 응답")
public record CancelPaymentResponse(
    @Schema(description = "취소 처리 성공 여부", required = true) boolean isSuccess,
    @Schema(description = "결제 상태", example = "결제 취소됨", required = true) String status,
    @Schema(description = "취소 요청 시간", required = true) LocalDateTime cancelRequestedAt) {
  private CancelPaymentResponse(boolean isSuccess, Payment payment) {
    this(isSuccess, payment.getStatus().getDescription(), payment.getUpdatedAt());
  }

  public static CancelPaymentResponse from(boolean isSuccess, Payment payment) {
    return new CancelPaymentResponse(isSuccess, payment);
  }
}
