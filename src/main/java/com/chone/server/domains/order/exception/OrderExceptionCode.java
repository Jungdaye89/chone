package com.chone.server.domains.order.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum OrderExceptionCode implements ExceptionCode {
  NOT_FOUND_ORDER(NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
  MULTIPLE_STORE_ORDER(BAD_REQUEST, "주문 상품은 동일한 가게의 상품이어야 합니다."),
  MISSING_DELIVERY_ADDRESS(BAD_REQUEST, "배송지 주소는 필수입니다."),
  FORBIDDEN_ORDER(FORBIDDEN, "주문은 사용자 또는 사장님이 할 수 있습니다."),
  NEGATIVE_PRICE(BAD_REQUEST, "가격은 음수일 수 없습니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
