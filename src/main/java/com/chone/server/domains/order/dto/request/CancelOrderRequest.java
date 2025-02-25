package com.chone.server.domains.order.dto.request;

import com.chone.server.domains.order.domain.OrderCancelReason;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "주문 취소 요청")
public record CancelOrderRequest(
    @Schema(description = "취소 사유", implementation = OrderCancelReason.class, required = true)
        @NotNull(message = "취소 이유는 필수입니다.")
        OrderCancelReason reasonNum) {}
