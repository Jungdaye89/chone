package com.chone.server.domains.payment.dto.response;

import com.chone.server.domains.payment.domain.PaymentMethod;
import com.chone.server.domains.payment.domain.PaymentStatus;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "결제 목록 페이지 응답")
public record PaymentPageResponse(
    @Schema(
            description = "결제 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        UUID id,
    @Schema(
            description = "주문 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        UUID orderId,
    @Schema(description = "총 결제 금액", example = "15000", required = true) int totalPrice,
    @Schema(description = "결제 수단", example = "카드", required = true) String method,
    @Schema(description = "결제 상태", example = "결제 완료", required = true) String status,
    @Schema(description = "결제 생성 시간", required = true) LocalDateTime createdAt,
    @Schema(description = "결제 수정 시간", required = true) LocalDateTime updatedAt) {

  @QueryProjection
  public PaymentPageResponse(
      UUID id,
      UUID orderId,
      int totalPrice,
      PaymentMethod method,
      PaymentStatus status,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this(
        id,
        orderId,
        totalPrice,
        method.getDescription(),
        status.getDescription(),
        createdAt,
        updatedAt);
  }
}
