package com.chone.server.domains.store.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class SearchResponseDto {

  private List<ReadResponseDto> content;
  private PageInfoDto pageInfo;
}