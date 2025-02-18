package com.chone.server.domains.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class PageInfoDto {

  private int page;
  private int size;
  private long totalElements;
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
