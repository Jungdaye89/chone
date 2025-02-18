package com.chone.server.domains.ai.dto.request;

import java.util.UUID;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiRequestDto {

  private UUID storeId;
  private UUID productId;
  private String text;
}
