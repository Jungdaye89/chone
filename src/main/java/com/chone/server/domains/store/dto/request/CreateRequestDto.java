package com.chone.server.domains.store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

  @NotNull(message = "사용자 ID는 필수 입력값 입니다.")
  private Long userId;

  @NotBlank(message = "가게 이름은 필수 입력값 입니다.")
  @Size(max = 50, message = "최대 50글자입니다.")
  private String name;

  @NotBlank(message = "시도는 필수 입력값 입니다.")
  @Size(max = 40, message = "최대 40글자입니다.")
  private String sido;

  @NotBlank(message = "시군구는 필수 입력값 입니다.")
  @Size(max = 40, message = "최대 40글자입니다.")
  private String sigungu;

  @NotBlank(message = "동은 필수 입력값 입니다.")
  @Size(max = 40, message = "최대 40글자입니다.")
  private String dong;

  @NotBlank(message = "상세주소는 필수 입력값 입니다.")
  @Size(max = 150, message = "최대 150글자 입니다.")
  private String address;

  @NotBlank(message = "카테고리는 필수 입력값 입니다.")
  private List<String> category;

  @NotBlank(message = "전화번호는 필수 입력값 입니다.")
  @Size(max = 30, message = "최대 30글자 입니다.")
  private String phoneNumber;
}