package com.chone.server.domains.review.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReviewResponseDTO {

  private UUID reviewId;
  private LocalDateTime createdAt;
}
