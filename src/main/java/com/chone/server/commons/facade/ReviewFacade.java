package com.chone.server.commons.facade;

import com.chone.server.domains.review.domain.Review;
import com.chone.server.domains.review.dto.response.ReviewDetailResponseDto;
import com.chone.server.domains.review.dto.response.ReviewStatisticsResponseDto;
import com.chone.server.domains.user.domain.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewFacade {

  List<Review> findAllReviews(Long userId);

  List<ReviewDetailResponseDto> findReviewsByUserId(Long userId);

  ReviewDetailResponseDto findReviewDetailById(UUID reviewId);

  Review findReviewById(UUID reviewId);

  Optional<Review> findByOrderId(UUID orderId);

  void deleteReview(User user, Review review);

  ReviewStatisticsResponseDto getReviewStatistics(UUID storeId);
}
