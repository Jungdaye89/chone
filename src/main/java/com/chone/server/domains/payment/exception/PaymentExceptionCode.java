package com.chone.server.domains.payment.exception;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PaymentExceptionCode implements ExceptionCode {
  CONFLICT_ALREADY_PAID(HttpStatus.CONFLICT, "이미 결제가 진행된 주문입니다."),
  ;
  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
