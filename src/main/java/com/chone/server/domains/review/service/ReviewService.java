package com.chone.server.domains.review.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.order.domain.OrderStatus;
import com.chone.server.domains.order.repository.OrderRepository;
import com.chone.server.domains.review.domain.Review;
import com.chone.server.domains.review.dto.request.CreateRequestDto;
import com.chone.server.domains.review.dto.request.ReviewListRequestDto;
import com.chone.server.domains.review.dto.response.ReviewListResponseDto;
import com.chone.server.domains.review.dto.response.ReviewResponseDto;
import com.chone.server.domains.review.exception.ReviewExceptionCode;
import com.chone.server.domains.review.repository.ReviewRepository;
import com.chone.server.domains.review.repository.ReviewSearchRepository;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.repository.StoreRepository;
import com.chone.server.domains.user.domain.User;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

  private final ReviewRepository reviewRepository;
  private final OrderRepository orderRepository;
  private final StoreRepository storeRepository;
  private final ReviewSearchRepository reviewSearchRepository;

  @Transactional
  public ReviewResponseDto createReview(CreateRequestDto request, User user) {

    Order order = orderRepository.findById(request.getOrderId());
    if (order == null) {
      throw new ApiBusinessException(ReviewExceptionCode.ORDER_NOT_FOUND);
    }

    Store store =
        storeRepository
            .findById(request.getStoreId())
            .orElseThrow(() -> new ApiBusinessException(ReviewExceptionCode.STORE_NOT_FOUND));

    if (!order.getUser().getId().equals(user.getId())) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_FORBIDDEN);
    }

    if (!order.getStatus().equals(OrderStatus.COMPLETED)) {
      throw new ApiBusinessException(ReviewExceptionCode.ORDER_NOT_COMPLETED);
    }

    if (reviewRepository.findByOrderId(order.getId()).isPresent()) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_ALREADY_EXISTS);
    }

    if (request.getRating().compareTo(BigDecimal.ONE) < 0
        || request.getRating().compareTo(BigDecimal.valueOf(5)) > 0) {
      throw new ApiBusinessException(ReviewExceptionCode.INVALID_RATING);
    }

    Review review =
        Review.builder(order, store, user, request.getContent(), request.getRating(), true)
            .imageUrl(request.getImageUrl())
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
}
