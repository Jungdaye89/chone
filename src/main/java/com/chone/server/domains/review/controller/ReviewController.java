package com.chone.server.domains.review.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.review.dto.request.CreateRequestDto;
import com.chone.server.domains.review.dto.request.ReviewListRequestDto;
import com.chone.server.domains.review.dto.response.ReviewListResponseDto;
import com.chone.server.domains.review.dto.response.ReviewResponseDto;
import com.chone.server.domains.review.service.ReviewService;
import java.util.Map;
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

    ReviewListRequestDto requestDto = ReviewListRequestDto.from(params);
    Pageable pageable = requestDto.toPageable();

    ReviewListResponseDto response = reviewService.getReviews(requestDto, principal, pageable);
    return ResponseEntity.ok(response);
  }
}
