package com.chone.server.domains.payment.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PgPaymentLogExceptionCode implements ExceptionCode {
  NOT_FOUND_PG_PAYMENT_LOG(NOT_FOUND, "해당 내역을 찾을 수 없습니다."),
  ;

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
