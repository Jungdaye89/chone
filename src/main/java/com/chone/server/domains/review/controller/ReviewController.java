package com.chone.server.domains.review.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.review.dto.request.CreateRequestDto;
import com.chone.server.domains.review.dto.request.DeleteRequestDto;
import com.chone.server.domains.review.dto.request.ListRequestDto;
import com.chone.server.domains.review.dto.request.UpdateRequestDto;
import com.chone.server.domains.review.dto.response.ReviewDeleteResponseDto;
import com.chone.server.domains.review.dto.response.ReviewDetailResponseDto;
import com.chone.server.domains.review.dto.response.ReviewListResponseDto;
import com.chone.server.domains.review.dto.response.ReviewResponseDto;
import com.chone.server.domains.review.dto.response.ReviewStatisticsResponseDto;
import com.chone.server.domains.review.dto.response.ReviewUpdateResponseDto;
import com.chone.server.domains.review.service.ReviewService;
import jakarta.validation.Valid;
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

  @PreAuthorize("hasRole('CUSTOMER')")
  @PostMapping
  public ResponseEntity<ReviewResponseDto> createReview(
      @AuthenticationPrincipal CustomUserDetails principal, @RequestBody CreateRequestDto request) {

    ReviewResponseDto response = reviewService.createReview(request, principal.getUser());

    return ResponseEntity.ok(response);
  }

  @GetMapping
  public ResponseEntity<ReviewListResponseDto> getReviews(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestParam Map<String, String> params) {

    ListRequestDto requestDto = ListRequestDto.from(params);
    Pageable pageable = requestDto.toPageable();

    ReviewListResponseDto response = reviewService.getReviews(requestDto, principal, pageable);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReviewDetailResponseDto> getReview(
      @PathVariable("id") UUID id, @AuthenticationPrincipal CustomUserDetails principal) {

    ReviewDetailResponseDto response = reviewService.getReviewById(id, principal);
    return ResponseEntity.ok(response);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ReviewUpdateResponseDto> updateReview(
      @PathVariable("id") UUID id,
      @Valid @RequestBody UpdateRequestDto request,
      @AuthenticationPrincipal CustomUserDetails principal) {

    ReviewUpdateResponseDto response = reviewService.updateReview(id, request, principal);
    return ResponseEntity.ok(response);
  }

  @PreAuthorize("hasAnyRole('CUSTOMER', 'MANAGER', 'MASTER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<ReviewDeleteResponseDto> deleteReview(
      @PathVariable("id") UUID id,
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestBody(required = false) @Valid DeleteRequestDto requestDto) {

    ReviewDeleteResponseDto response = reviewService.deleteReview(id, principal, requestDto);
    return ResponseEntity.ok(response);
  }

  @GetMapping("/statistics")
  public ResponseEntity<ReviewStatisticsResponseDto> getReviewStatistics(
      @RequestParam(name = "storeId") UUID storeId) {

    ReviewStatisticsResponseDto response = reviewService.getReviewStatistics(storeId);
    return ResponseEntity.ok(response);
  }
}
