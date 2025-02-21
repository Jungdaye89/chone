package com.chone.server.domains.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentStatus {
  PENDING("결제 대기 중"),
  ACCEPTED("결제 승인됨"),
  COMPLETED("결제 완료"),
  CANCEL_REQUESTED("결제 취소 요청됨"),
  CANCELED("결제 취소됨"),
  FAILED("결제 실패");
  private final String description;
}
