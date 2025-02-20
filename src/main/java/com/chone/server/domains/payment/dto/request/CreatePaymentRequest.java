package com.chone.server.domains.payment.dto.request;

import com.chone.server.domains.payment.domain.PaymentMethod;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record CreatePaymentRequest(
    @NotNull(message = "주문 ID는 필수입니다") UUID orderId,
    @NotNull(message = "총 가격은 필수입니다") @Min(value = 0, message = "총 가격은 0 이상이어야 합니다") int totalPrice,
    @NotNull(message = "결제 방법은 필수입니다") PaymentMethod paymentMethod) {}
