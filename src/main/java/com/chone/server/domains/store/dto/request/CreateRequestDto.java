package com.chone.server.domains.store.dto.request;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
@NotNull
public class CreateRequestDto {

  private Long userId;
  private String name;
  private String sido;
  private String sigungu;
  private String dong;
  private String address;
  private List<String> category;
  private String phoneNumber;
}