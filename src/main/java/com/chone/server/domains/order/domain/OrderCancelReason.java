package com.chone.server.domains.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderCancelReason {
  CUSTOMER_REQUEST(1, "고객 요청"),
  OUT_OF_STOCK(2, "재고 부족"),
  STORE_CLOSED(3, "가게 영업 종료"),
  DELIVERY_ISSUE(4, "배달 문제"),
  PAYMENT_FAILED(5, "결제 실패"),
  ORDER_ERROR(6, "주문 입력 오류"),
  POLICY_VIOLATION(7, "정책 위반"),
  ADMIN_APPROVED_CANCEL(8, "고객 요청 승인"),
  ADMIN_DECISION(9, "관리자 판단"),
  CANCEL_PAYMENT(10, "결제취소"),

  OTHER(99, "기타");

  private final int num;
  private final String description;
}
