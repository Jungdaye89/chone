package com.chone.server.domains.payment.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
  CASH("현금"),
  CARD("카드");

  private final String description;
}
