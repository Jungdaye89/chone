package com.chone.server.domains.ai.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GeminiApiResponseDto {
  private List<Candidate> candidates;

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class Candidate {

    private Content content;
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
