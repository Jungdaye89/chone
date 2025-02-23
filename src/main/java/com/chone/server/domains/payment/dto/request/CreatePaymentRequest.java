package com.chone.server.domains.payment.dto.request;

import com.chone.server.domains.payment.domain.PaymentMethod;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;

@Schema(description = "결제 생성 요청")
public record CreatePaymentRequest(
    @Schema(
            description = "주문 ID",
            example = "123e4567-e89b-12d3-a456-426614174000",
            required = true)
        @NotNull(message = "주문 ID는 필수입니다")
        UUID orderId,
    @Schema(description = "총 결제 금액", example = "15000", minimum = "0", required = true)
        @NotNull(message = "총 가격은 필수입니다")
        @Size(min = 1, message = "총 가격은 0 이상이어야 합니다")
        int totalPrice,
    @Schema(description = "결제 수단", implementation = PaymentMethod.class, required = true)
        @NotNull(message = "결제 방법은 필수입니다")
        PaymentMethod paymentMethod) {}
