package com.chone.server.domains.product.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Schema(description = "상품 검색 응답 Dto")
public class SearchResponseDto {

  @Schema(description = "내용")
  private List<ReadResponseDto> content;

  @Schema(description = "페이지 정보")
  private PageInfoDto pageInfo;
}