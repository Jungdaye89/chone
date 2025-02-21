package com.chone.server.domains.order.dto.request;

import com.chone.server.domains.order.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record OrderStatusUpdateRequest(@NotNull OrderStatus status) {}
