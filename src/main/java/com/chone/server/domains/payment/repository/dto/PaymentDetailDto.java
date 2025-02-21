package com.chone.server.domains.payment.repository.dto;

import com.chone.server.domains.order.domain.OrderType;
import com.chone.server.domains.payment.domain.PaymentMethod;
import com.chone.server.domains.payment.domain.PaymentStatus;
import com.chone.server.domains.payment.dto.response.PaymentDetailResponse;
import java.util.UUID;

public record PaymentDetailDto(
    UUID paymentId,
    PaymentStatus paymentStatus,
    String cancelReason,
    PaymentMethod paymentMethod,
    UUID orderId,
    int totalPrice,
    OrderType orderType,
    Long storeUserId,
    Long userId,
    UUID storeId,
    String storeName) {

  public PaymentDetailResponse toResponse() {
    return new PaymentDetailResponse(
        new PaymentDetailResponse.PaymentResponse(
            paymentId,
            paymentStatus.getDescription(),
            cancelReason,
            paymentMethod.getDescription()),
        new PaymentDetailResponse.OrderResponse(
            orderId,
            totalPrice,
            orderType.getDescription(),
            userId,
            storeId,
            storeName,
            storeUserId));
  }
}
