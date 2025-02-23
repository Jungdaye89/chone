package com.chone.server.domains.review.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.OrderFacade;
import com.chone.server.commons.facade.ReviewFacade;
import com.chone.server.commons.facade.StoreFacade;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.review.domain.Review;
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
import com.chone.server.domains.review.exception.ReviewExceptionCode;
import com.chone.server.domains.review.repository.ReviewRepository;
import com.chone.server.domains.review.repository.ReviewSearchRepository;
import com.chone.server.domains.review.repository.ReviewStatisticsRepository;
import com.chone.server.domains.s3.service.S3Service;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.repository.StoreRepository;
import com.chone.server.domains.user.domain.User;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final StoreRepository storeRepository;
  private final ReviewSearchRepository reviewSearchRepository;
  private final ReviewStatisticsRepository reviewStatisticsRepository;
  private final ReviewFacade reviewFacade;
  private final StoreFacade storeFacade;
  private final OrderFacade orderFacade;
  private final S3Service s3Service;

  @Transactional
  public ReviewResponseDto createReview(CreateRequestDto request, MultipartFile file, User user) {

    Order order = orderFacade.findById(request.getOrderId());
    orderFacade.validateOrderOwnership(order, user);
    orderFacade.validateOrderStatus(order, OrderStatus.COMPLETED);

    Store store = storeFacade.findStoreById(request.getStoreId());

    if (reviewFacade.findByOrderId(order.getId()).isPresent()) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_ALREADY_EXISTS);
    }

    if (request.getRating().compareTo(BigDecimal.ONE) < 0
        || request.getRating().compareTo(BigDecimal.valueOf(5)) > 0) {
      throw new ApiBusinessException(ReviewExceptionCode.INVALID_RATING);
    }

    String imageUrl = request.getImageUrl();
    if (file != null && !file.isEmpty()) {
      imageUrl = s3Service.uploadFile(file);
    }

    Review review =
        Review.builder(order, store, user, request.getContent(), request.getRating(), true)
            .imageUrl(imageUrl)
            .build();

    Review savedReview = reviewRepository.save(review);

    return new ReviewResponseDto(savedReview.getId(), savedReview.getCreatedAt());
  }

  public ReviewListResponseDto getReviews(
      ReviewListRequestDto request, CustomUserDetails principal, Pageable pageable) {
    User user = principal.getUser();

    return switch (user.getRole()) {
      case CUSTOMER ->
          ReviewListResponseDto.from(
              reviewSearchRepository.findReviewsByCustomer(user, request, pageable));
      case OWNER ->
          ReviewListResponseDto.from(
              reviewSearchRepository.findReviewsByOwner(user, request, pageable));
      case MANAGER, MASTER ->
          ReviewListResponseDto.from(
              reviewSearchRepository.findReviewsByManagerOrMaster(user, request, pageable));
    };
  }

  public ReviewDetailResponseDto getReviewById(UUID reviewId, CustomUserDetails principal) {
    if (principal == null || principal.getUser() == null) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_UNAUTHORIZED);
    }

    ReviewDetailResponseDto reviewDetail = reviewFacade.findReviewDetailById(reviewId);

    validateAccess(principal.getUser(), reviewDetail);

    return reviewDetail;
  }

  public List<ReviewDetailResponseDto> getReviewsByUserId(
      Long userId, CustomUserDetails principal) {

    if (principal == null
        || principal.getUser() == null
        || !principal.getUser().getId().equals(userId)) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_FORBIDDEN_ACTION);
    }

    return reviewFacade.findReviewsByUserId(userId);
  }

  private void validateAccess(User user, ReviewDetailResponseDto reviewDetail) {
    if (reviewDetail.getIsPublic() != null
        && !reviewDetail.getIsPublic()
        && !reviewDetail.getCustomerInfo().getCustomerId().equals(user.getId())) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_FORBIDDEN_ACTION);
    }
  }

  @Transactional
  public ReviewUpdateResponseDto updateReview(
      UUID reviewId, UpdateRequestDto request, MultipartFile file, CustomUserDetails principal) {

    if (principal == null || principal.getUser() == null) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_UNAUTHORIZED);
    }

    Review review = reviewFacade.findReviewById(reviewId);

    if (!review.getUser().getId().equals(principal.getUser().getId())) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_FORBIDDEN_ACTION);
    }

    if (ChronoUnit.HOURS.between(review.getCreatedAt(), LocalDateTime.now()) > 72) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_UPDATE_EXPIRED);
    }

    String imageUrl = review.getImageUrl();
    if (file != null && !file.isEmpty()) {
      if (imageUrl != null) {
        s3Service.removeFile(imageUrl);
      }
      imageUrl = s3Service.uploadFile(file);
    }

    review.update(request.getContent(), request.getRating(), imageUrl, request.getIsPublic());

    return new ReviewUpdateResponseDto(review.getId(), review.getUpdatedAt());
  }

  @Transactional
  public ReviewDeleteResponseDto deleteReview(
      UUID reviewId, CustomUserDetails principal, DeleteRequestDto requestDto) {

    if (principal == null || principal.getUser() == null) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_UNAUTHORIZED);
    }

    Review review = reviewFacade.findReviewById(reviewId);

    if (review.getDeletedAt() != null) {
      throw new ApiBusinessException(ReviewExceptionCode.ALREADY_DELETED);
    }

    switch (principal.getUser().getRole()) {
      case CUSTOMER -> {
        if (!review.getUser().getId().equals(principal.getUser().getId())) {
          throw new ApiBusinessException(ReviewExceptionCode.REVIEW_FORBIDDEN);
        }
      }

      case MANAGER, MASTER -> {
        if (requestDto == null
            || requestDto.getReason() == null
            || requestDto.getReason().isBlank()) {
          throw new ApiBusinessException(ReviewExceptionCode.INVALID_REQUEST);
        }
      }
      default -> throw new ApiBusinessException(ReviewExceptionCode.REVIEW_FORBIDDEN);
    }

    review.softDelete(principal.getUser(), requestDto != null ? requestDto.getReason() : null);
    reviewRepository.save(review);

    return new ReviewDeleteResponseDto(review.getId(), review.getDeletedAt());
  }

  @Transactional(readOnly = true)
  public ReviewStatisticsResponseDto getReviewStatistics(UUID storeId) {

    boolean storeExists = storeRepository.existsById(storeId);
    if (!storeExists) {
      throw new ApiBusinessException(ReviewExceptionCode.STORE_NOT_FOUND);
    }

    BigDecimal averageRating = reviewStatisticsRepository.calculateAverageRating(storeId);

    int totalReviews = reviewStatisticsRepository.countTotalReviews(storeId);

    Map<Integer, Integer> ratingDistribution =
        reviewStatisticsRepository.countRatingDistribution(storeId);

    LocalDateTime lastUpdatedAt = LocalDateTime.now();

    return new ReviewStatisticsResponseDto(
        averageRating, totalReviews, ratingDistribution, lastUpdatedAt);
  }
}
