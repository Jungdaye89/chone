package com.chone.server.domains.payment.dto.request;

import com.chone.server.domains.payment.domain.PaymentMethod;
import com.chone.server.domains.payment.domain.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "결제 조회 필터")
public record PaymentFilterParams(
    @Schema(description = "조회 시작일 (yyyy-MM-dd)", example = "2024-02-24") LocalDate startDate,
    @Schema(description = "조회 종료일 (yyyy-MM-dd)", example = "2024-02-24") LocalDate endDate,
    @Schema(description = "가게 ID", example = "123e4567-e89b-12d3-a456-426614174000") UUID storeId,
    @Schema(description = "사용자 ID", example = "12345") Long userId,
    @Schema(description = "결제 상태", implementation = PaymentStatus.class) PaymentStatus status,
    @Schema(description = "결제 수단", implementation = PaymentMethod.class) PaymentMethod method,
    @Schema(description = "정확한 결제 금액", example = "15000") Integer totalPrice,
    @Schema(description = "최소 결제 금액", example = "10000") Integer minPrice,
    @Schema(description = "최대 결제 금액", example = "50000") Integer maxPrice) {}
