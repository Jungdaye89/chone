package com.chone.server.domains.review.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.review.dto.request.CreateRequestDto;
import com.chone.server.domains.review.dto.request.DeleteRequestDto;
import com.chone.server.domains.review.dto.request.ReviewListRequestDto;
import com.chone.server.domains.review.dto.request.UpdateRequestDto;
import com.chone.server.domains.review.dto.response.ReviewDeleteResponseDto;
import com.chone.server.domains.review.dto.response.ReviewDetailResponseDto;
import com.chone.server.domains.review.dto.response.ReviewListResponseDto;
import com.chone.server.domains.review.dto.response.ReviewResponseDto;
import com.chone.server.domains.review.dto.response.ReviewStatisticsResponseDto;
import com.chone.server.domains.review.dto.response.ReviewUpdateResponseDto;
import com.chone.server.domains.review.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @Operation(summary = "리뷰 생성 API", description = "사용자가 새로운 리뷰를 작성한다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "리뷰 생성 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReviewResponseDto.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 200,
                                    "message": "리뷰 생성 성공",
                                    "data": {
                                        "reviewId": "리뷰 ID",
                                        "createdAt": "리뷰 작성 시간"
                                    }
                                }
                                """))),
    @ApiResponse(
        responseCode = "403",
        description = "리뷰 생성 실패",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 403,
                                    "message": "리뷰 생성 실패",
                                    "code": "FAIL"
                                }
                                """)))
  })
  @PreAuthorize("hasRole('CUSTOMER')")
  @PostMapping
  public ResponseEntity<ReviewResponseDto> createReview(
      @AuthenticationPrincipal CustomUserDetails principal, @RequestBody CreateRequestDto request) {

    ReviewResponseDto response = reviewService.createReview(request, principal.getUser());

    return ResponseEntity.ok(response);
  }

  @Operation(summary = "리뷰 목록 조회 API", description = "리뷰 전체 목록을 조회한다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "리뷰 목록 조회 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReviewListResponseDto.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 200,
                                    "message": "리뷰 목록 조회 성공",
                                    "data": {
                                        "content": [
                                            {
                                                "reviewId": "리뷰 ID",
                                                "orderId": "주문 ID",
                                                "storeId": "가게 ID",
                                                "customerId": "고객 ID",
                                                "content": "리뷰 내용",
                                                "imageUrl": "이미지 URL",
                                                "rating": "리뷰 평점",
                                                "writtenAt": "작성 시간"
                                            }
                                        ],
                                        "pageInfo": {
                                            "page": 0,
                                            "size": 10,
                                            "totalElements": 100,
                                            "totalPages": 10
                                        }
                                    }
                                }
                                """))),
    @ApiResponse(
        responseCode = "403",
        description = "리뷰 목록 조회 실패",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 403,
                                    "message": "리뷰 목록 조회 실패",
                                    "code": "FAIL"
                                }
                                """)))
  })
  @GetMapping
  public ResponseEntity<ReviewListResponseDto> getReviews(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestParam Map<String, String> params) {

    ReviewListRequestDto requestDto = ReviewListRequestDto.from(params);
    Pageable pageable = requestDto.toPageable();

    ReviewListResponseDto response = reviewService.getReviews(requestDto, principal, pageable);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "리뷰 상세 조회 API", description = "리뷰의 상세 정보를 조회한다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "리뷰 상세 조회 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReviewDetailResponseDto.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 200,
                                    "message": "리뷰 상세 조회 성공",
                                    "data": {
                                        "reviewId": "리뷰 ID",
                                        "orderId": "주문 ID",
                                        "storeInfo": {
                                            "storeId": "가게 ID",
                                            "storeName": "가게 이름"
                                        },
                                        "customerInfo": {
                                            "customerId": "고객 ID",
                                            "username": "고객 이름"
                                        },
                                        "content": "리뷰 내용",
                                        "rating": "리뷰 평점",
                                        "imageUrl": "이미지 URL",
                                        "writtenAt": "작성 시간",
                                        "isPublic": true
                                    }
                                }
                                """))),
    @ApiResponse(
        responseCode = "403",
        description = "리뷰 상세 조회 실패",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 403,
                                    "message": "리뷰 상세 조회 실패",
                                    "code": "FAIL"
                                }
                                """)))
  })
  @GetMapping("/{id}")
  public ResponseEntity<ReviewDetailResponseDto> getReview(
      @PathVariable("id") UUID id, @AuthenticationPrincipal CustomUserDetails principal) {

    ReviewDetailResponseDto response = reviewService.getReviewById(id, principal);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "유저가 작성한 리뷰 목록 조회 API", description = "특정 유저가 작성한 모든 리뷰 목록을 조회한다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "리뷰 목록 조회 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReviewDetailResponseDto.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 200,
                                    "message": "리뷰 목록 조회 성공",
                                    "data": [
                                        {
                                            "reviewId": "리뷰 ID 1",
                                            "orderId": "주문 ID 1",
                                            "storeInfo": {
                                                "storeId": "가게 ID 1",
                                                "storeName": "가게 이름 1"
                                            },
                                            "customerInfo": {
                                                "customerId": "고객 ID 1",
                                                "username": "고객 이름"
                                            },
                                            "content": "리뷰 내용 1",
                                            "rating": 4.5,
                                            "imageUrl": "이미지 URL 1",
                                            "writtenAt": "작성 시간 1",
                                            "isPublic": true
                                        },
                                        {
                                            "reviewId": "리뷰 ID 2",
                                            "orderId": "주문 ID 2",
                                            "storeInfo": {
                                                "storeId": "가게 ID 2",
                                                "storeName": "가게 이름 2"
                                            },
                                            "customerInfo": {
                                                "customerId": "고객 ID 2",
                                                "username": "고객 이름"
                                            },
                                            "content": "리뷰 내용 2",
                                            "rating": 5.0,
                                            "imageUrl": "이미지 URL 2",
                                            "writtenAt": "작성 시간 2",
                                            "isPublic": true
                                        }
                                    ]
                                }
                                """))),
    @ApiResponse(
        responseCode = "403",
        description = "리뷰 목록 조회 실패",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 403,
                                    "message": "리뷰 목록 조회 실패",
                                    "code": "FAIL"
                                }
                                """)))
  })
  @PreAuthorize("hasRole('CUSTOMER')")
  @GetMapping("/user/{id}")
  public ResponseEntity<List<ReviewDetailResponseDto>> getReviewsByUserId(
      @PathVariable("id") Long userId, @AuthenticationPrincipal CustomUserDetails principal) {

    List<ReviewDetailResponseDto> response = reviewService.getReviewsByUserId(userId, principal);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "리뷰 수정 API", description = "사용자가 작성한 리뷰 내용을 3일 이내에 수정한다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "리뷰 수정 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReviewUpdateResponseDto.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 200,
                                    "message": "리뷰 수정 성공",
                                    "data": {
                                        "reviewId": "리뷰 ID",
                                        "updatedAt": "수정 시간"
                                    }
                                }
                                """))),
    @ApiResponse(
        responseCode = "403",
        description = "리뷰 수정 실패",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 403,
                                    "message": "리뷰 수정 실패",
                                    "code": "FAIL"
                                }
                                """)))
  })
  @PutMapping("/{id}")
  public ResponseEntity<ReviewUpdateResponseDto> updateReview(
      @PathVariable("id") UUID id,
      @Valid @RequestBody UpdateRequestDto request,
      @AuthenticationPrincipal CustomUserDetails principal) {

    ReviewUpdateResponseDto response = reviewService.updateReview(id, request, principal);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "리뷰 삭제 API", description = "사용자가 작성한 리뷰를 삭제한다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "리뷰 삭제 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReviewDeleteResponseDto.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 200,
                                    "message": "리뷰 삭제 성공",
                                    "data": {
                                        "reviewId": "리뷰 ID",
                                        "deletedAt": "삭제 시간"
                                    }
                                }
                                """))),
    @ApiResponse(
        responseCode = "403",
        description = "리뷰 삭제 실패",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 403,
                                    "message": "리뷰 삭제 실패",
                                    "code": "FAIL"
                                }
                                """)))
  })
  @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER', 'MASTER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<ReviewDeleteResponseDto> deleteReview(
      @PathVariable("id") UUID id,
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestBody(required = false) @Valid DeleteRequestDto requestDto) {

    ReviewDeleteResponseDto response = reviewService.deleteReview(id, principal, requestDto);
    return ResponseEntity.ok(response);
  }

  @Operation(summary = "리뷰 통계 조회 API", description = "리뷰 통계를 조회한다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "리뷰 통계 조회 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ReviewStatisticsResponseDto.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 200,
                                    "message": "리뷰 통계 조회 성공",
                                    "data": {
                                        "averageRating": 4.5,
                                        "totalReviews": 100,
                                        "ratingDistribution": {
                                            "5": "5개를 받은 개수",
                                            "4": "4개를 받은 개수",
                                            "3": "3개를 받은 개수",
                                            "2": "2개를 받은 개수",
                                            "1": "1개를 받은 개수"
                                        },
                                        "lastUpdatedAt": "업데이트 시간"
                                    }
                                }
                                """))),
    @ApiResponse(
        responseCode = "403",
        description = "리뷰 통계 조회 실패",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 403,
                                    "message": "리뷰 통계 조회 실패",
                                    "code": "FAIL"
                                }
                                """)))
  })
  @GetMapping("/statistics")
  public ResponseEntity<ReviewStatisticsResponseDto> getReviewStatistics(
      @RequestParam(name = "storeId") UUID storeId) {

    ReviewStatisticsResponseDto response = reviewService.getReviewStatistics(storeId);
    return ResponseEntity.ok(response);
  }
}
