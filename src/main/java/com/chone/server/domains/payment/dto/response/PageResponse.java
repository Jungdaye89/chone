package com.chone.server.domains.payment.dto.response;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponse<T>(List<T> content, PageContent pageInfo) {
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

  private record PageContent(
      int page, int size, long totalElements, int totalPages, boolean last) {}
}
