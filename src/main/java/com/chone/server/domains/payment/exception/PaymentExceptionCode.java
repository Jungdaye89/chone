package com.chone.server.domains.payment.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PaymentExceptionCode implements ExceptionCode {
  CUSTOMER_OFFLINE_ORDER(FORBIDDEN, "고객은 오프라인 주문에 대한 결제 권한이 없습니다."),
  NOT_OWNER_STORE(FORBIDDEN, "해당 매장의 점주만 결제할 수 있습니다."),
  NOT_ORDER_CUSTOMER(FORBIDDEN, "주문자만 결제할 수 있습니다."),
  CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED(FORBIDDEN, "사용자별 주문 조회 권한이 없습니다."),
  STORE_PAYMENT_FILTERING_ACCESS_DENIED(FORBIDDEN, "가게별 주문 조회 권한이 없습니다."),

  ALREADY_PAID(CONFLICT, "이미 결제가 진행된 주문입니다."),

  NOT_FOUND_PAYMENT(NOT_FOUND, "해당 결제 내역을 찾을 수 없습니다."),

  CANCELED_ORDER(BAD_REQUEST, "취소된 주문은 결제할 수 없습니다."),
  PRICE_MISMATCH(BAD_REQUEST, "결제 금액이 주문 금액과 일치하지 않습니다."),

  PAYMENT_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "결제 처리 중 오류가 발생했습니다."),
  PAYMENT_IN_PROGRESS(HttpStatus.SERVICE_UNAVAILABLE, "결제가 진행 중입니다. 잠시 후 다시 시도해주세요."),
  PAYMENT_GATEWAY_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "결제 시스템 연동 중 오류가 발생했습니다."),
  ;

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
