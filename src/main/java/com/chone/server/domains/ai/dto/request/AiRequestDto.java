package com.chone.server.domains.ai.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.*;

@Schema(description = "AI 요청 DTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRequestDto {

  @Schema(description = "가게 ID", example = "e4d1f5d2-3c89-4b99-aeef-2b4665d707a3")
  @NotNull(message = "{storeId.required}")
  private UUID storeId;

  @Schema(description = "상품 ID", example = "acb12345-6789-0abc-def1-234567890abc")
  @NotNull(message = "{productId.required}")
  private UUID productId;

  @Schema(description = "AI가 처리할 텍스트", example = "이 상품에 대해 설명해줘")
  @NotNull(message = "{text.required}")
  @Size(min = 1, message = "{text.size}")
  private String text;
}
