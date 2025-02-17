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
public class UpdateRequestDto {

  private String name;
  private List<String> category;
  private String phoneNumber;
  private String sido;
  private String sigungu;
  private String dong;
  private String address;
  private boolean isOpen;
  private boolean isPublic;
}