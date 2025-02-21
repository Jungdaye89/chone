package com.chone.server.domains.payment.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum PaymentExceptionCode implements ExceptionCode {
  NOT_ALLOW_PAY_OFFLINE_ORDER(FORBIDDEN, "가게는 배달 주문에 대한 결제 권한이 없습니다."),
  NOT_ALLOW_PAY_ONLINE_ORDER(FORBIDDEN, "고객은 현장 결제에 대한 결제 권한이 없습니다."),

  NOT_OWNER_PAYMENT(FORBIDDEN, "해당 가게의 주인만 결제할 수 있습니다."),
  NOT_CUSTOMER_PAYMENT(FORBIDDEN, "주문자만 결제할 수 있습니다."),
  NOT_OWNER_PAYMENT_HISTORY(FORBIDDEN, "해당 가게의 주인만 결제 내역을 조회할 수 있습니다."),
  NOT_CUSTOMER_PAYMENT_HISTORY(FORBIDDEN, "주문자만 결제 내역을 조회할 수 있습니다."),

  CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED(FORBIDDEN, "사용자별 주문 조회 권한이 없습니다."),
  STORE_PAYMENT_FILTERING_ACCESS_DENIED(FORBIDDEN, "가게별 주문 조회 권한이 없습니다."),

  ALREADY_PAID(CONFLICT, "이미 결제가 진행된 주문입니다."),

  NOT_FOUND_PAYMENT(NOT_FOUND, "해당 결제 내역을 찾을 수 없습니다."),

  CANCELED_ORDER(BAD_REQUEST, "취소된 주문은 결제할 수 없습니다."),
  PRICE_MISMATCH(BAD_REQUEST, "결제 금액이 주문 금액과 일치하지 않습니다."),

  PAYMENT_PROCESSING_ERROR(INTERNAL_SERVER_ERROR, "결제 처리 중 오류가 발생했습니다."),
  PAYMENT_IN_PROGRESS(SERVICE_UNAVAILABLE, "결제가 진행 중입니다. 잠시 후 다시 시도해주세요."),
  PAYMENT_GATEWAY_ERROR(SERVICE_UNAVAILABLE, "결제 시스템 연동 중 오류가 발생했습니다."),
  PAYMENT_ALREADY_CANCELED(CONFLICT, "이미 취소된 결제입니다"),
  FAILED_PAYMENT(BAD_REQUEST, "실패된 결제입니다(결제된 적이 없습니다)."),
  ORDER_NOT_CANCELABLE(BAD_REQUEST, "주문이 취소될 수 없어 결제를 취소할 수 없습니다."),

  PAYMENT_CANCELLATION_IN_PROGRESS(SERVICE_UNAVAILABLE, "결제 취소가 진행 중입니다. 잠시 후 다시 시도해주세요."),
  PAYMENT_CANCELLATION_ERROR(INTERNAL_SERVER_ERROR, "결제 취소 처리 중 오류가 발생했습니다."),

  PAYMENT_CANCELLATION_FAILED(SERVICE_UNAVAILABLE, "결제 취소 요청이 거부되었습니다.");
  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
