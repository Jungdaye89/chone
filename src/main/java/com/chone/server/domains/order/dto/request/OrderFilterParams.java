package com.chone.server.domains.order.dto.request;

import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.domain.OrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "주문 조회 필터")
public record OrderFilterParams(
    @Schema(description = "조회 시작일 (yyyy-MM-dd)", example = "2024-02-24") LocalDate startDate,
    @Schema(description = "조회 종료일 (yyyy-MM-dd)", example = "2024-02-24") LocalDate endDate,
    @Schema(
            description = "가게 ID (OWNER 권한의 경우 필터링 제한됨)",
            example = "123e4567-e89b-12d3-a456-426614174000")
        UUID storeId,
    @Schema(description = "사용자 ID (CUSTOMER 권한의 경우 필터링 제한됨)", example = "12345") Long userId,
    @Schema(description = "주문 상태", implementation = OrderStatus.class) OrderStatus status,
    @Schema(description = "주문 유형", implementation = OrderType.class) OrderType orderType,
    @Schema(description = "최소 주문금액", example = "10000") Integer minPrice,
    @Schema(description = "최대 주문금액", example = "50000") Integer maxPrice) {}
