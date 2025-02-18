package com.chone.server.domains.ai.dto.response;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiResponseDto {

  private UUID id;
  private UUID storeId;
  private UUID productId;
  private String requestText;
  private String responseText;
}
