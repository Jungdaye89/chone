package com.chone.server.domains.order.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(enumAsRef = true, description = "주문 상태")
@Getter
@RequiredArgsConstructor
public enum OrderStatus {
  @Schema(description = "주문이 접수되어 가게 확인 대기 중")
  PENDING(10, "주문 접수"),

  @Schema(description = "가게에서 주문을 수락함")
  ACCEPTED(20, "주문 수락"),

  @Schema(description = "고객 결제 완료")
  PAID(30, "주문 결제 완료"),

  @Schema(description = "음식 조리 중")
  FOOD_PREPARING(40, "음식 준비 중"),

  @Schema(description = "음식 조리 완료")
  FOOD_PREPARED(50, "음식 준비 완료"),

  @Schema(description = "배달 라이더 배정 대기 중")
  AWAITING_DELIVERY(60, "배달 배차 기다리는 중"),

  @Schema(description = "배달 진행 중")
  IN_DELIVERY(70, "배달 중"),

  @Schema(description = "주문 완료")
  COMPLETED(80, "주문 작업 및 픽업 또는 배달 완료"),

  @Schema(description = "주문 취소됨")
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

  public boolean isSameStatus(OrderStatus status) {
    return this == status;
  }
}
