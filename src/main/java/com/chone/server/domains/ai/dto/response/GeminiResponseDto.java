package com.chone.server.domains.ai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Gemini 응답 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeminiResponseDto {

  @Schema(description = "AI 생성 텍스트", example = "이 음식은 고급 소재로 만들어졌으며 맛이 뛰어납니다.")
  private String text;
}
