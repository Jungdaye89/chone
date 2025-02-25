package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "주문 목록 페이지 응답")
public record OrderPageResponse(
    @Schema(
            description = "주문 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        UUID id,
    @Schema(
            description = "가게 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        UUID storeId,
    @Schema(description = "가게 이름", example = "맛있는 피자", required = true) String storeName,
    @Schema(description = "주문 유형", example = "배달", required = true) String type,
    @Schema(description = "주문 상태", example = "주문 접수", required = true) String status,
    @Schema(description = "총 주문 금액", example = "25000", required = true) int totalPrice,
    @Schema(description = "주문 생성 시간", required = true) LocalDateTime createdAt,
    @Schema(description = "주문 수정 시간", required = true) LocalDateTime updatedAt) {

  @QueryProjection
  public OrderPageResponse(
      UUID id,
      UUID storeId,
      String storeName,
      OrderType type,
      OrderStatus status,
      int totalPrice,
      LocalDateTime createdAt,
      LocalDateTime updatedAt) {
    this(
        id,
        storeId,
        storeName,
        type.getDescription(),
        status.getDescription(),
        totalPrice,
        createdAt,
        updatedAt);
  }
}
