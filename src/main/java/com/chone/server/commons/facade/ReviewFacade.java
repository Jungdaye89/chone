package com.chone.server.commons.facade;

import com.chone.server.domains.review.domain.Review;
import com.chone.server.domains.review.dto.response.ReviewDetailResponseDto;
import com.chone.server.domains.user.domain.User;

import java.util.List;
import java.util.UUID;

public interface ReviewFacade {

  List<Review> findAllReviews(Long userId);
  List<ReviewDetailResponseDto> findReviewsByUserId(Long userId);
  ReviewDetailResponseDto findReviewDetailById(UUID reviewId);
  void deleteReview(User user, Review review);
}
