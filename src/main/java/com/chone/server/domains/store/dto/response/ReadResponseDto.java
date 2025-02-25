package com.chone.server.domains.store.dto.response;

import com.chone.server.domains.store.domain.Store;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "가게 조회 응답 Dto")
public class ReadResponseDto {

  @Schema(description = "가게 ID")
  private UUID id;

  @Schema(description = "가게 이름")
  private String name;

  @Schema(description = "시도명")
  private String sido;

  @Schema(description = "시군구명")
  private String sigungu;

  @Schema(description = "동명")
  private String dong;

  @Schema(description = "상세주소")
  private String address;

  @Schema(description = "카테고리")
  private List<String> category;

  @Schema(description = "전화번호")
  private String phoneNumber;

  @Schema(description = "가게 오픈 여부")
  private Boolean isOpen;

  @Schema(description = "평균 평점")
  private Double rating;

  public static ReadResponseDto from(Store store, Double avgRating) {

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
        .phoneNumber(store.getPhoneNumber())
        .isOpen(store.getIsOpen())
        .rating(avgRating)
        .build();
  }
}