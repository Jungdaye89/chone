package com.chone.server.domains.user.dto.response;

import com.chone.server.domains.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "유저 검색 조회 응답 DTO")
public class SearchUserResponseDto {
  @Schema(description = "유저 응답 리스트",
  example = "\"status\": 200,\n"
      + "    \"message\": \"회원목록 조회 성공\",\n"
      + "    \"code\": \"SUCCESS\",\n"
      + "    \"data\": {\n"
      + "        \"content\": [\n"
      + "            {\n"
      + "                \"id\": 11,\n"
      + "                \"username\": \"customer12\",\n"
      + "                \"email\": \"customer12@example.com\",\n"
      + "                \"role\": \"CUSTOMER\",\n"
      + "                \"createdAt\": \"2025-02-23T17:51:16.178461\"\n"
      + "            },\n"
      + "            {\n"
      + "                \"id\": 2,\n"
      + "                \"username\": \"testMaster\",\n"
      + "                \"email\": \"testMaster@example.com\",\n"
      + "                \"role\": \"MASTER\",\n"
      + "                \"createdAt\": \"2025-02-20T01:48:02.257833\"\n"
      + "            }\n"
      + "        ]")
  private List<ReadResponseDto> content;
  @Schema(
      description = "페이지 정보 DTO",
      example = "{\n" +
          "  \"pageInfo\": {\n" +
          "    \"page\": 0,\n" +
          "    \"size\": 10,\n" +
          "    \"totalElements\": 6,\n" +
          "    \"totalPages\": 1\n" +
          "  }\n" +
          "}"
  )
  private PageInfoDto pageInfo;
}
