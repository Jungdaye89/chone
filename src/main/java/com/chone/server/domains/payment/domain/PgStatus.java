package com.chone.server.domains.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PgStatus {
  PROCESSING_SUCCESS("결제 처리 성공"),
  PROCESSING_FAILED("결제 처리 실패"),
  PROCESSING_ERROR("결제 처리 중 오류"),

  CANCEL_SUCCESS("결제 취소 성공"),
  CANCEL_FAILED("결제 취소 실패"),
  CANCEL_ERROR("결제 취소 오류");

  private final String description;
}
