package com.chone.server.domains.store.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Schema(description = "페이지 정보 Dto")
public class PageInfoDto {

  @Schema(description = "현재 페이지")
  private int page;

  @Schema(description = "현재 페이지 당 데이터 수")
  private int size;

  @Schema(description = "전체 데이터 수")
  private long totalElements;

  @Schema(description = "전체 페이지 수")
  private int totalPages;
}