package com.chone.server.domains.payment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "결제 취소 요청")
public record CancelPaymentRequest(
    @Schema(description = "취소 이유", example = "고객 요청으로 인한 취소", maxLength = 25, required = true)
        @NotBlank(message = "취소 이유는 필수입니다.")
        @Size(max = 25, message = "취소 이유는 25자 내로 작성할 수 있습니다.")
        String reason) {}
