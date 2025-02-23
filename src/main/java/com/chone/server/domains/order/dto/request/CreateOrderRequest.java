package com.chone.server.domains.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Schema(description = "주문 생성 요청")
public record CreateOrderRequest(
    @Schema(
            description = "가게 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        @NotNull(message = "가게 ID는 필수입니다")
        UUID storeId,
    @Schema(description = "주문 상품 목록", required = true) @NotEmpty(message = "주문 상품은 최소 1개 이상이어야 합니다")
        List<@Valid OrderItemRequest> orderItems,
    @Schema(description = "배달 주소, ONLINE 주문일 경우 필수.", example = "서울시 강남구 역삼동 123-45")
        String address,
    @Schema(description = "요청사항", example = "문 앞에 놓아주세요") String requestText) {
  @Schema(description = "주문 상품 정보")
  public record OrderItemRequest(
      @Schema(
              description = "상품 ID",
              example = "123e4567-e89b-12d3-a456-426614174000",
              required = true)
          @NotNull(message = "상품 ID는 필수입니다")
          UUID productId,
      @Schema(description = "주문 수량", example = "2", minimum = "1", required = true)
          @Size(min = 1, message = "수량은 1개 이상이어야 합니다")
          @NotNull(message = "수량은 필수입니다")
          Integer quantity) {}
}
