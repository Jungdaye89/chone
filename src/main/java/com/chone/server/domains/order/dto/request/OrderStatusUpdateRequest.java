package com.chone.server.domains.order.dto.request;

import com.chone.server.domains.order.domain.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "주문 상태 변경 요청")
public record OrderStatusUpdateRequest(
    @Schema(description = "변경할 주문 상태", implementation = OrderStatus.class, required = true) @NotNull
        OrderStatus status) {}
