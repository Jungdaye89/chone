package com.chone.server.domains.order.dto.request;

import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import java.time.LocalDate;
import java.util.UUID;

public record OrderFilterParams(
    LocalDate startDate,
    LocalDate endDate,
    UUID storeId,
    Long userId,
    OrderStatus status,
    OrderType orderType,
    Integer minPrice,
    Integer maxPrice) {}
