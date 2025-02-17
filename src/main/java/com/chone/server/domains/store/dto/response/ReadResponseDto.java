package com.chone.server.domains.store.dto.response;

import com.chone.server.domains.store.domain.Store;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReadResponseDto {

  private UUID id;
  private String name;
  private String sido;
  private String sigungu;
  private String dong;
  private String address;
  private List<String> category;
  private Boolean isOpen;

  public static ReadResponseDto from(Store store) {

    List<String> categoryNames = store.getStoreCategoryMaps().stream()
        .map(scm -> scm.getCategory().getName())
        .collect(Collectors.toList());

    return ReadResponseDto.builder()
        .id(store.getId())
        .name(store.getName())
        .sido(store.getSido())
        .sigungu(store.getSigungu())
        .dong(store.getDong())
        .address(store.getAddress())
        .category(categoryNames)
        .isOpen(store.getIsOpen())
        .build();
  }
}