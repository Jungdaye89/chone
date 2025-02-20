package com.chone.server.domains.payment.dto.response;

import com.chone.server.domains.payment.domain.PaymentMethod;
import com.chone.server.domains.payment.domain.PaymentStatus;
import java.time.LocalDateTime;
import java.util.UUID;

public record CreatePaymentResponse(
    UUID id,
    UUID orderId,
    int totalPrice,
    PaymentStatus status,
    PaymentMethod paymentMethod,
    LocalDateTime createdAt) {}
