package com.chone.server.domains.payment.dto.request;

import com.chone.server.domains.payment.domain.PaymentMethod;
import com.chone.server.domains.payment.domain.PaymentStatus;
import java.time.LocalDate;
import java.util.UUID;

public record PaymentFilterParams(
    LocalDate startDate,
    LocalDate endDate,
    UUID storeId,
    Long userId,
    PaymentStatus status,
    PaymentMethod method,
    Integer totalPrice,
    Integer minPrice,
    Integer maxPrice) {}
