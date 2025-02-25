package com.chone.server.domains.payment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import org.springframework.data.domain.Page;

@Schema(description = "페이지네이션 응답")
public record PageResponse<T>(
    @Schema(
            description = "페이지 컨텐츠",
            required = true,
            subTypes = {PaymentPageResponse.class})
        List<T> content,
    @Schema(description = "페이지 정보", required = true) PageContent pageInfo) {
  private PageResponse(Page<T> page) {
    this(
        page.getContent(),
        new PageContent(
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()));
  }

  public static PageResponse from(Page page) {
    return new PageResponse(page);
  }

  @Schema(description = "페이지 정보")
  private record PageContent(
      @Schema(description = "현재 페이지 번호", example = "0", required = true) int page,
      @Schema(description = "페이지 크기", example = "10", required = true) int size,
      @Schema(description = "전체 항목 수", example = "100", required = true) long totalElements,
      @Schema(description = "전체 페이지 수", example = "10", required = true) int totalPages,
      @Schema(description = "마지막 페이지 여부", required = true) boolean last) {}
}
