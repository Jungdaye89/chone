package com.chone.server.domains.review.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "리뷰 목록 조회 요청 DTO")
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReviewListRequestDto {

  @Schema(description = "페이지 번호 (0부터 시작)", example = "0")
  private Integer page;

  @Schema(description = "페이지 크기", example = "10")
  private Integer size;

  @Schema(description = "정렬 기준 필드", example = "createdAt")
  private String sort;

  @Schema(description = "정렬 방향 (asc 또는 desc)", example = "desc")
  private String direction;

  @Schema(description = "가게 ID", example = "e4d1f5d2-3c89-4b99-aeef-2b4665d707a3")
  private UUID storeId;

  @Schema(description = "고객 ID", example = "123456789")
  private Long customerId;

  @Schema(description = "주문 ID", example = "acb12345-6789-0abc-def1-234567890abc")
  private UUID orderId;

  @Schema(description = "최소 평점 필터", example = "3.0")
  private Double minRating;

  @Schema(description = "최대 평점 필터", example = "5.0")
  private Double maxRating;

  @Schema(description = "이미지 포함 여부 필터", example = "true")
  private Boolean hasImage;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "리뷰 작성 시작일", example = "2023-01-01")
  private String startDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @Schema(description = "리뷰 작성 종료일", example = "2023-12-31")
  private String endDate;

  public static ReviewListRequestDto from(Map<String, String> params) {
    int defaultPage = 0;
    int defaultSize = 10;

    String pageParam = params.get("page");
    String sizeParam = params.get("size");

    int page;

    if (pageParam == null || pageParam.isEmpty()) {
      page = defaultPage;
    } else {
      try {
        page = Integer.parseInt(pageParam);
      } catch (NumberFormatException e) {
        page = defaultPage;
      }
    }

    int size;
    if (sizeParam == null || sizeParam.isEmpty()) {
      size = defaultSize;
    } else {
      try {
        size = Integer.parseInt(sizeParam);
      } catch (NumberFormatException e) {
        size = defaultSize;
      }
    }

    return ReviewListRequestDto.builder()
        .page(page)
        .size(size)
        .sort(params.getOrDefault("sort", "createdAt"))
        .direction(params.getOrDefault("direction", "desc"))
        .storeId(params.containsKey("storeId") ? UUID.fromString(params.get("storeId")) : null)
        .customerId(
            params.containsKey("customerId") ? Long.parseLong(params.get("customerId")) : null)
        .orderId(params.containsKey("orderId") ? UUID.fromString(params.get("orderId")) : null)
        .minRating(
            params.containsKey("minRating") ? Double.parseDouble(params.get("minRating")) : null)
        .maxRating(
            params.containsKey("maxRating") ? Double.parseDouble(params.get("maxRating")) : null)
        .hasImage(
            params.containsKey("hasImage") ? Boolean.parseBoolean(params.get("hasImage")) : null)
        .startDate(params.get("startDate"))
        .endDate(params.get("endDate"))
        .build();
  }

  public Pageable toPageable() {
    int defaultPage = 0;
    int defaultSize = 10;
    String defaultSort = "createdAt";
    String defaultDirection = "desc";

    int page = (this.page != null) ? this.page : defaultPage;
    int size = (this.size != null) ? this.size : defaultSize;
    String sortField = (this.sort != null) ? this.sort : defaultSort;
    String sortDirection = (this.direction != null) ? this.direction : defaultDirection;

    Sort.Direction direction =
        "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

    return PageRequest.of(page, size, Sort.by(direction, sortField));
  }
}
