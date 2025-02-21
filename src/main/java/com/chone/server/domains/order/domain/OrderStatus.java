package com.chone.server.domains.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
  PENDING(10, "주문 접수"),
  ACCEPTED(20, "주문 수락"),
  PAID(30, "주문 결제 완료"),
  FOOD_PREPARING(40, "음식 준비 중"),
  FOOD_PREPARED(50, "음식 준비 완료"),
  AWAITING_DELIVERY(60, "배달 배차 기다리는 중"),
  IN_DELIVERY(70, "배달 중"),
  COMPLETED(80, "주문 작업 및 픽업 또는 배달 완료"),
  CANCELED(0, "주문 취소");

  private final int step;
  private final String description;

  public boolean isProgressableFrom(OrderStatus currentStatus) {
    if (this == CANCELED || this == PAID) {
      return false;
    }
    return this.step > currentStatus.step;
  }

  public boolean isValidForOfflineOrder() {
    return this != AWAITING_DELIVERY && this != IN_DELIVERY;
  }

  public boolean isTerminal() {
    return this == COMPLETED || this == CANCELED;
  }
}
