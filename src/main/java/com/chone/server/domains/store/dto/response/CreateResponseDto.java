package com.chone.server.domains.store.dto.response;

import com.chone.server.domains.store.domain.Store;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateResponseDto {

  private UUID id;

  public CreateResponseDto(Store store) {
    this.id = store.getId();
  }
}