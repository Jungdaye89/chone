package com.chone.server.domains.review.facade;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.ReviewFacade;
import com.chone.server.domains.review.domain.Review;
import com.chone.server.domains.review.dto.response.ReviewDetailResponseDto;
import com.chone.server.domains.review.exception.ReviewExceptionCode;
import com.chone.server.domains.review.repository.ReviewDetailSearchRepository;
import com.chone.server.domains.review.repository.ReviewRepository;
import com.chone.server.domains.user.domain.User;
import com.chone.server.domains.user.exception.UserExceptionCode;
import com.chone.server.domains.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewFacadeImpl implements ReviewFacade {

  private final ReviewRepository reviewRepository;
  private final ReviewDetailSearchRepository reviewDetailSearchRepository;
  private final UserRepository userRepository;

  @Override
  public List<Review> findAllReviews(Long userId) {
    return reviewRepository.findAllByUserId(userId);
  }

  @Override
  public List<ReviewDetailResponseDto> findReviewsByUserId(Long userId) {

    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));

    return reviewRepository.findAllByUserId(user.getId()).stream()
        .map(ReviewDetailResponseDto::from)
        .collect(Collectors.toList());
  }

  @Override
  public ReviewDetailResponseDto findReviewDetailById(UUID reviewId) {

    return reviewDetailSearchRepository.findReviewDetailById(reviewId);
  }

  @Override
  public Review findReviewById(UUID reviewId) {
    return reviewRepository
        .findByIdAndDeletedAtIsNull(reviewId)
        .orElseThrow(() -> new ApiBusinessException(ReviewExceptionCode.REVIEW_NOT_FOUND));
  }

  @Override
  public Optional<Review> findByOrderId(UUID orderId) {
    return reviewRepository.findByOrderId(orderId);
  }

  @Override
  public void deleteReview(User user, Review review) {
    review.delete(user);
  }
}
