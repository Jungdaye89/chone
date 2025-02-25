package com.chone.server.domains.review.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "리뷰 수정 응답 DTO")
@Getter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ReviewUpdateResponseDto {

  @Schema(description = "수정된 리뷰 ID", example = "d9b12345-6789-0abc-def1-234567890abc")
  private UUID reviewId;

  @Schema(description = "리뷰 수정 시간", example = "2025-02-22T11:00:00")
  private LocalDateTime updatedAt;
}
