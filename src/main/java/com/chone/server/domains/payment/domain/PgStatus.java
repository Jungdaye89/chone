package com.chone.server.domains.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PgStatus {
  SUCCESS("PG사 승인됨"),
  FAILED("PG사 처리 실패"),
  PENDING("PG사 처리 대기");

  private final String description;
}
