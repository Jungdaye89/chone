package com.chone.server.domains.order.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;

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
  NEGATIVE_PRICE(BAD_REQUEST, "가격은 음수일 수 없습니다."),
  INVALID_QUANTITY(BAD_REQUEST, "수량은 0보다 커야 합니다."),
  ORDER_STORE_CLOSED(SERVICE_UNAVAILABLE, "현재 가게가 영업 중이 아닙니다."),
  ORDER_PRODUCT_MISMATCH(BAD_REQUEST, "주문한 상품이 존재하지 않거나 일부 상품이 누락되었습니다."),
  ORDER_PRODUCT_UNAVAILABLE(BAD_REQUEST, "주문할 수 없는 상품입니다."),
  ORDER_FILTERING_ACCESS_DENIED(FORBIDDEN, "사용자 또는 가게별 주문 조회 권한이 없습니다."),
  STORE_ORDER_FILTERING_ACCESS_DENIED(FORBIDDEN, "가게별 주문 조회 권한이 없습니다."),
  ORDER_CUSTOMER_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다. 해당 주문의 소유자가 아닙니다."),
  ORDER_STORE_OWNER_ACCESS_DENIED(FORBIDDEN, "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다."),

  ORDER_CANCELLATION_TIMEOUT(BAD_REQUEST, "주문 생성 후 5분이 경과하여 취소할 수 없습니다."),
  ORDER_CANCEL_PERMISSION_DENIED(FORBIDDEN, "주문 취소 권한이 없습니다."),
  ORDER_ALREADY_CANCELED(BAD_REQUEST, "이미 취소된 주문입니다."),
  ORDER_NOT_CANCELABLE(BAD_REQUEST, "취소할 수 없는 주문입니다."),
  ORDER_NOT_DELETABLE(CONFLICT, "주문 과정이 완료되지 않아 삭제할 수 없는 주문입니다."),
  ;
  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
