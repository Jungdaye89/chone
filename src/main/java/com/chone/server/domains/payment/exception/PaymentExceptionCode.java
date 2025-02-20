package com.chone.server.domains.payment.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PaymentExceptionCode implements ExceptionCode {
  ORDER_ACCESS(FORBIDDEN, "해당 주문에 대한 접근 권한이 없습니다."),
  CUSTOMER_OFFLINE_ORDER(FORBIDDEN, "고객은 오프라인 주문에 대한 결제 권한이 없습니다."),
  OWNER_ONLINE_ORDER(FORBIDDEN, "점주는 온라인 주문에 대한 결제 권한이 없습니다."),
  NOT_OWNER_STORE(FORBIDDEN, "해당 매장의 점주만 결제할 수 있습니다."),
  NOT_ORDER_CUSTOMER(FORBIDDEN, "주문자만 결제할 수 있습니다."),

  ALREADY_PAID(CONFLICT, "이미 결제가 진행된 주문입니다."),

  CANCELED_ORDER(BAD_REQUEST, "취소된 주문은 결제할 수 없습니다."),
  EXPIRED_ORDER(BAD_REQUEST, "결제 가능 시간이 만료되었습니다."),
  PRICE_MISMATCH(BAD_REQUEST, "결제 금액이 주문 금액과 일치하지 않습니다."),
  ;
  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
