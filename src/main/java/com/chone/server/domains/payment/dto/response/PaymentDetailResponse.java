package com.chone.server.domains.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "결제 상세 정보 응답")
public record PaymentDetailResponse(
    @Schema(description = "결제 정보", required = true) PaymentResponse payment,
    @Schema(description = "주문 정보", required = true) OrderResponse order) {
  @Schema(description = "결제 상세 정보")
  public record PaymentResponse(
      @Schema(
              description = "결제 ID",
              example = "123e4567-e89b-12d3-a456-426614174000",
              required = true)
          UUID id,
      @Schema(description = "결제 상태", example = "결제 완료", required = true) String status,
      @Schema(description = "취소 사유", example = "고객 요청으로 인한 취소") String cancelReason,
      @Schema(description = "결제 수단", example = "카드", required = true) String method) {}

  @Schema(description = "주문 상세 정보")
  public record OrderResponse(
      @Schema(
              description = "주문 ID",
              example = "123e4567-e89b-12d3-a456-426614174000",
              required = true)
          UUID id,
      @Schema(description = "총 주문 금액", example = "15000", required = true) int totalPrice,
      @Schema(description = "주문 유형", example = "배달", required = true) String type,
      @Schema(description = "주문자 ID", example = "12345", required = true) Long userId,
      @Schema(
              description = "가게 ID",
              example = "123e4567-e89b-12d3-a456-426614174000",
              required = true)
          UUID storeId,
      @Schema(description = "가게 이름", example = "맛있는 피자", required = true) String storeName,
      @Schema(hidden = true) Long storeUserId) {}
}
