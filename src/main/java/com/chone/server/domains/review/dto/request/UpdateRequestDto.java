package com.chone.server.domains.review.dto.request;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateRequestDto {

  private String content;
  private BigDecimal rating;
  private String imageUrl;
  private Boolean isPublic;
}
