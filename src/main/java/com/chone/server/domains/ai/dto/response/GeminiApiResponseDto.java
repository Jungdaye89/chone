package com.chone.server.domains.ai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Gemini API 응답 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeminiApiResponseDto {

  @Schema(description = "AI 응답 리스트")
  private List<Candidate> candidates;

  @Schema(description = "AI 응답 객체")
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Candidate {

    @Schema(description = "AI 응답 내용")
    private Content content;
  }

  @Schema(description = "AI 응답 내용 객체")
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Content {

    @Schema(description = "AI 응답 파트 리스트")
    private List<Part> parts;
  }

  @Schema(description = "AI 응답 파트 객체")
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Part {

    @Schema(description = "AI 응답 텍스트", example = "이 음식은 고급 소재로 만들어졌으며 맛이 뛰어납니다.")
    private String text;
  }
}
