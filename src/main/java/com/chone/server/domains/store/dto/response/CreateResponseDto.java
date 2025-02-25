package com.chone.server.domains.store.dto.response;

import com.chone.server.domains.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "가게 생성 응답 Dto")
public class CreateResponseDto {

  @Schema(description = "가게 ID")
  private UUID id;

  public CreateResponseDto(Store store) {

    this.id = store.getId();
  }
}