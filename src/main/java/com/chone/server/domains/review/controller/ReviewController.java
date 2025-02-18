package com.chone.server.domains.review.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.review.dto.request.CreateRequestDTO;
import com.chone.server.domains.review.dto.response.ReviewResponseDTO;
import com.chone.server.domains.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
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
  public ResponseEntity<ReviewResponseDTO> createReview(
      @AuthenticationPrincipal CustomUserDetails principal, @RequestBody CreateRequestDTO request) {

    ReviewResponseDTO response = reviewService.createReview(request, principal.getUser());

    return ResponseEntity.ok(response);
  }
}
