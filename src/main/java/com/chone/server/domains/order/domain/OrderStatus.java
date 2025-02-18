package com.chone.server.domains.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
  PENDING("주문 접수"),
  ACCEPTED("주문 수락"),
  FOOD_PREPARING("음식 준비 중"),
  FOOD_PREPARED("음식 준비 완료"),
  AWAITING_DELIVERY("배달 접수 중"),
  IN_DELIVERY("배달 중"),
  COMPLETED("주문 작업 및 픽업 완료"),
  CANCELED("주문 취소");

  private final String description;
}
