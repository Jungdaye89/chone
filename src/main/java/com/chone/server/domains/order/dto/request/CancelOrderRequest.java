package com.chone.server.domains.order.dto.request;

import com.chone.server.domains.order.domain.OrderCancelReason;
import jakarta.validation.constraints.NotNull;

public record CancelOrderRequest(@NotNull(message = "취소 이유는 필수입니다.") OrderCancelReason reasonNum) {}
