package com.chone.server.domains.payment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "결제 상태")
@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
  @Schema(description = "결제 대기 중 - 결제 진행 전 초기 상태")
  PENDING("결제 대기 중"),

  @Schema(description = "결제 완료 - 성공적으로 결제가 완료된 상태")
  COMPLETED("결제 완료"),

  @Schema(description = "결제 취소됨 - 결제가 취소된 상태")
  CANCELED("결제 취소됨"),

  @Schema(description = "결제 실패 - 결제 진행 중 실패한 상태")
  FAILED("결제 실패");

  private final String description;

  public boolean isSameStatus(PaymentStatus status) {
    return this == status;
  }
}
