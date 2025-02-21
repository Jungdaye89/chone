package com.chone.server.commons.facade;

import com.chone.server.domains.review.domain.Review;
import com.chone.server.domains.review.dto.response.ReviewDetailResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewFacade {

  List<Review> findAllReviews(Long userId);
  List<ReviewDetailResponseDto> findReviewsByUserId(Long userId);

  ReviewDetailResponseDto findReviewDetailById(UUID reviewId);

  Review findReviewById(UUID reviewId);

  // OrderId로 Order에 리뷰가 달렸는지 확인하는 작업이기 때문에 Review에서 작업 진행했습니다
  // 이후 주석 제거 할 때 같이 제거할게요
  Optional<Review> findByOrderId(UUID orderId);
  void deleteReview(User user, Review review);
}
