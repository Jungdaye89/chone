package com.chone.server.domains.ai.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeminiApiRequestDto {

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

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Content {

    private List<Part> parts;
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Part {

    private String text;
  }
}
