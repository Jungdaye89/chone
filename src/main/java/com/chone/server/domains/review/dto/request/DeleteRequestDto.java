package com.chone.server.domains.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "리뷰 삭제 요청 DTO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class DeleteRequestDto {

  @Schema(description = "리뷰 삭제 사유", example = "잘못된 리뷰 작성")
  private String reason;
}
