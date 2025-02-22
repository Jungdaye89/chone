package com.chone.server.domains.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "리뷰 통계 조회 요청 DTO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class StatisticsRequestDto {

  @Schema(description = "가게 ID", example = "e4d1f5d2-3c89-4b99-aeef-2b4665d707a3")
  private UUID storeId;
}
