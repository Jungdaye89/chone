package com.chone.server.domains.review.dto.request;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
public class ReviewListRequestDto {

  private Integer page = 0;
  private Integer size = 10;
  private String sort = "createAt";
  private String direction = "desc";
  private UUID storeId;
  private Long customerId;
  private UUID orderId;
  private Integer minRating;
  private Integer maxRating;
  private Boolean hasImage;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String startDate;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private String endDate;
}
