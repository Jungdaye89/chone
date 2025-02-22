package com.chone.server.domains.ai.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "AI 응답 DTO")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiResponseDto {

  @Schema(description = "AI ID", example = "bcd12345-6789-0abc-def1-234567890abc")
  private UUID id;

  @Schema(description = "가게 ID", example = "e4d1f5d2-3c89-4b99-aeef-2b4665d707a3")
  private UUID storeId;

  @Schema(description = "상품 ID", example = "acb12345-6789-0abc-def1-234567890abc")
  private UUID productId;

  @Schema(description = "요청 텍스트", example = "이 음식에 대해 설명해줘")
  private String requestText;

  @Schema(description = "AI 응답 텍스트", example = "이 음식은 고급 소재로 만들어졌으며 맛이 뛰어납니다.")
  private String responseText;
}
