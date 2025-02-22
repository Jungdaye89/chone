package com.chone.server.domains.ai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "Gemini API 요청 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeminiApiRequestDto {

  @Schema(description = "AI 요청 내용 리스트")
  private List<Content> contents;

  public static GeminiApiRequestDto from(String text) {
    return GeminiApiRequestDto.builder()
        .contents(
            List.of(
                GeminiApiRequestDto.Content.builder()
                    .parts(List.of(GeminiApiRequestDto.Part.builder().text(text).build()))
                    .build()))
        .build();
  }

  @Schema(description = "요청 내용 객체")
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Content {

    @Schema(description = "요청 파트 리스트")
    private List<Part> parts;
  }

  @Schema(description = "요청 파트 객체")
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Part {

    @Schema(description = "AI 요청 텍스트", example = "이 상품의 특징은 무엇인가요?")
    private String text;
  }
}
