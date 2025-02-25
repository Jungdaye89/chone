package com.chone.server.domains.payment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "결제 수단")
@Getter
@RequiredArgsConstructor
public enum PaymentMethod {
  @Schema(description = "현금 결제")
  CASH("현금"),

  @Schema(description = "카드 결제")
  CARD("카드");

  private final String description;
}
