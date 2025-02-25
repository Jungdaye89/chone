package com.chone.server.domains.user.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Schema(description = "페이지 정보 DTO")
public class PageInfoDto {
  @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
  private int page;
  @Schema(description = "페이지 크기", example = "10")
  private int size;
  @Schema(description = "총 요소 개수", example = "100")
  private long totalElements;
  @Schema(description = "총 페이지 수", example = "10")
  private int totalPages;
}
