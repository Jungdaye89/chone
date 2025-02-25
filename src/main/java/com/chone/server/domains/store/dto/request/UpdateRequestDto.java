package com.chone.server.domains.store.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "가게 수정 요청 Dto")
public class UpdateRequestDto {

  @Schema(description = "가게 이름")
  @NotBlank(message = "가게 이름은 필수 입력값 입니다.")
  @Size(max = 50, message = "최대 50글자입니다.")
  private String name;

  @Schema(description = "카테고리")
  @NotBlank(message = "카테고리는 필수 입력값 입니다.")
  private List<String> category;

  @Schema(description = "전화번호")
  @NotBlank(message = "전화번호는 필수 입력값 입니다.")
  @Size(max = 30, message = "최대 30글자 입니다.")
  private String phoneNumber;

  @Schema(description = "시도명")
  @NotBlank(message = "시도는 필수 입력값 입니다.")
  private String sido;

  @Schema(description = "시군구명")
  @NotBlank(message = "시군구는 필수 입력값 입니다.")
  private String sigungu;

  @Schema(description = "동명")
  @NotBlank(message = "동은 필수 입력값 입니다.")
  private String dong;

  @Schema(description = "상세주소")
  @NotBlank(message = "상세주소는 필수 입력값 입니다.")
  @Size(max = 150, message = "최대 150글자 입니다.")
  private String address;

  @Schema(description = "가게 오픈 여부")
  @NotNull(message = "가게 오픈 여부는 필수 입력값 입니다.")
  private Boolean isOpen;

  @Schema(description = "정보 공개 여부")
  @NotNull(message = "정보 공개 여부는 필수 입력값 입니다.")
  private Boolean isPublic;
}