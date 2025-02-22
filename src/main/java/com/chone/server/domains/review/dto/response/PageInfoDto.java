package com.chone.server.domains.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Schema(description = "페이지 정보 DTO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class PageInfoDto {

  @Schema(description = "현재 페이지 번호 (0부터 시작)", example = "0")
  private int page;

  @Schema(description = "페이지 크기", example = "10")
  private int size;

  @Schema(description = "총 요소 개수", example = "100")
  private long totalElements;

  @Schema(description = "총 페이지 수", example = "10")
  private int totalPages;

  public static <T> PageInfoDto from(Page<T> page) {
    return PageInfoDto.builder()
        .page(page.getNumber())
        .size(page.getSize())
        .totalElements(page.getTotalElements())
        .totalPages(page.getTotalPages())
        .build();
  }
}
