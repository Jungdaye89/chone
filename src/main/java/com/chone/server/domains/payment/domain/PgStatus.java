package com.chone.server.domains.payment.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Schema(description = "PG사 결제 처리 상태")
@Getter
@RequiredArgsConstructor
public enum PgStatus {
  @Schema(description = "PG사 결제 처리가 성공적으로 완료됨")
  PROCESSING_SUCCESS("결제 처리 성공"),

  @Schema(description = "PG사 결제 처리 중 실패 발생")
  PROCESSING_FAILED("결제 처리 실패"),

  @Schema(description = "PG사 결제 처리 중 시스템 오류 발생")
  PROCESSING_ERROR("결제 처리 중 오류"),

  @Schema(description = "PG사 결제 취소가 성공적으로 완료됨")
  CANCEL_SUCCESS("결제 취소 성공"),

  @Schema(description = "PG사 결제 취소 중 실패 발생")
  CANCEL_FAILED("결제 취소 실패"),

  @Schema(description = "PG사 결제 취소 중 시스템 오류 발생")
  CANCEL_ERROR("결제 취소 오류");

  private final String description;
}
