package com.chone.server.domains.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "리뷰 삭제 응답 DTO")
@Getter
@AllArgsConstructor
public class ReviewDeleteResponseDto {

  @Schema(description = "삭제된 리뷰 ID", example = "d9b12345-6789-0abc-def1-234567890abc")
  private UUID reviewId;

  @Schema(description = "삭제 시간", example = "2025-02-22T10:30:00")
  private LocalDateTime deletedAt;
}
