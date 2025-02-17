package com.chone.server.domains.order.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum OrderExceptionCode implements ExceptionCode {
  NOT_FOUND_ORDER(NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
  MULTIPLE_STORE_ORDER(BAD_REQUEST, "주문 상품은 동일한 가게의 상품이어야 합니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
