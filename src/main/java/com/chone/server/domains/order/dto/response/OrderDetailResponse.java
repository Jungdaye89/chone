package com.chone.server.domains.order.dto.response;

import com.chone.server.domains.order.domain.OrderCancelReason;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.UUID;

@Schema(description = "주문 상세 조회 응답")
public record OrderDetailResponse(
    @Schema(description = "주문 정보", required = true) OrderResponse order,
    @Schema(description = "사용자 정보", required = true) UserResponse user,
    @Schema(description = "가게 정보", required = true) StoreResponse store,
    @Schema(description = "주문 상품 목록", required = true) List<OrderItemResponse> orderItems) {

  @QueryProjection
  public OrderDetailResponse {}

  @Schema(description = "주문 정보")
  public record OrderResponse(
      @Schema(
              description = "주문 ID",
              example = "123e4567-e89b-12d3-a456-426614174000",
              required = true)
          UUID id,
      @Schema(description = "주문 유형", example = "배달", required = true) String type,
      @Schema(description = "주문 상태", example = "주문 접수", required = true) String status,
      @Schema(description = "총 주문 금액", example = "25000", required = true) int totalPrice,
      @Schema(description = "취소 사유") String cancelReason,
      @Schema(description = "요청사항", example = "문 앞에 놓아주세요") String request) {

    @QueryProjection
    public OrderResponse(
        UUID id,
        OrderType type,
        OrderStatus status,
        int totalPrice,
        OrderCancelReason cancelReason,
        String request) {
      this(
          id,
          type.getDescription(),
          status.getDescription(),
          totalPrice,
          cancelReason != null ? cancelReason.getDescription() : null,
          request);
    }
  }

  @Schema(description = "사용자 정보")
  public record UserResponse(
      @Schema(description = "사용자 ID", example = "12345", required = true) Long id,
      @Schema(description = "사용자 이름", example = "홍길동", required = true) String username) {

    @QueryProjection
    public UserResponse {}
  }

  @Schema(description = "가게 정보")
  public record StoreResponse(
      @Schema(
              description = "가게 ID",
              example = "123e4567-e89b-12d3-a456-426614174000",
              required = true)
          UUID id,
      @Schema(description = "가게 이름", example = "맛있는 피자", required = true) String storeName) {
    @QueryProjection
    public StoreResponse {}
  }

  @Schema(description = "주문 상품 정보")
  public record OrderItemResponse(
      @Schema(
              description = "상품 ID",
              example = "123e4567-e89b-12d3-a456-426614174000",
              required = true)
          UUID id,
      @Schema(description = "상품명", example = "페퍼로니 피자", required = true) String productName,
      @Schema(description = "주문 금액", example = "25000", required = true) int amount) {
    @QueryProjection
    public OrderItemResponse {}
  }
}
