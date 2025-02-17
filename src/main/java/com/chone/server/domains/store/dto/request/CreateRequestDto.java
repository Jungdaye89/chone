package com.chone.server.domains.store.dto.request;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
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