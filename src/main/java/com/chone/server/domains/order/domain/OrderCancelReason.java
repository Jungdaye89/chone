package com.chone.server.domains.order.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(enumAsRef = true, description = "주문 취소 사유")
@Getter
@RequiredArgsConstructor
public enum OrderCancelReason {
  @Schema(description = "고객이 직접 취소를 요청")
  CUSTOMER_REQUEST(1, "고객 요청"),

  @Schema(description = "상품 재고 부족으로 인한 취소")
  OUT_OF_STOCK(2, "재고 부족"),

  @Schema(description = "가게 영업 시간 종료")
  STORE_CLOSED(3, "가게 영업 종료"),

  @Schema(description = "배달 관련 문제 발생")
  DELIVERY_ISSUE(4, "배달 문제"),

  @Schema(description = "결제 진행 중 오류 발생")
  PAYMENT_FAILED(5, "결제 실패"),

  @Schema(description = "주문 정보 입력 오류")
  ORDER_ERROR(6, "주문 입력 오류"),

  @Schema(description = "서비스 정책 위반")
  POLICY_VIOLATION(7, "정책 위반"),

  @Schema(description = "고객 취소 요청에 대한 관리자 승인")
  ADMIN_APPROVED_CANCEL(8, "고객 요청 승인"),

  @Schema(description = "관리자 직권 취소")
  ADMIN_DECISION(9, "관리자 판단"),

  @Schema(description = "결제 취소")
  CANCEL_PAYMENT(10, "결제취소"),

  @Schema(description = "기타 사유")
  OTHER(99, "기타");

  private final int num;
  private final String description;
}
