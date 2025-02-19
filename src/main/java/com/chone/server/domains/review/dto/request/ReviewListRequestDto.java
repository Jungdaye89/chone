package com.chone.server.domains.review.dto.request;

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

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReviewListRequestDto {

  private Integer page;
  private Integer size;
  private String sort;
  private String direction;
  private UUID storeId;
  private Long customerId;
  private UUID orderId;
  private Double minRating;
  private Double maxRating;
  private Boolean hasImage;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String startDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String endDate;

  public static ReviewListRequestDto from(Map<String, String> params) {
    return ReviewListRequestDto.builder()
        .page(params.containsKey("page") ? Integer.parseInt(params.get("page")) : 0)
        .size(params.containsKey("size") ? Integer.parseInt(params.get("size")) : 10)
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

    String[] sortParams = this.sort.split(",");

    String sortField = sortParams[0];

    String sortDirection = sortParams.length > 1 ? sortParams[1] : this.direction;

    Sort.Direction direction =
        "asc".equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;

    return PageRequest.of(this.page, this.size, Sort.by(direction, sortField));
  }
}
