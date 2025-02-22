package com.chone.server.domains.ai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Gemini 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeminiRequestDto {

  @Schema(description = "요청 텍스트", example = "이 제품에 대해 설명해 주세요")
  private String text;
}
