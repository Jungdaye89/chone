package com.chone.server.domains.payment.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CancelPaymentRequest(
    @NotBlank(message = "취소 이유는 필수입니다.") @Size(max = 25, message = "취소 이유는 25자 내로 작성할 수 있습니다.")
        String reason) {}
