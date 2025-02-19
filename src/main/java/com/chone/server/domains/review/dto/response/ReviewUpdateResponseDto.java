package com.chone.server.domains.review.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReviewUpdateResponseDto {

  private UUID reviewId;
  private LocalDateTime updatedAt;
}
