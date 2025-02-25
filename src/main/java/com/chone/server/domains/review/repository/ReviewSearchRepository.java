package com.chone.server.domains.review.repository;

import com.chone.server.domains.review.dto.request.ReviewListRequestDto;
import com.chone.server.domains.review.dto.response.ReviewPageResponseDto;
import com.chone.server.domains.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewSearchRepository {

  Page<ReviewPageResponseDto> findReviewsByCustomer(
      User customer, ReviewListRequestDto filterParams, Pageable pageable);

  Page<ReviewPageResponseDto> findReviewsByOwner(
      User owner, ReviewListRequestDto filterParams, Pageable pageable);

  Page<ReviewPageResponseDto> findReviewsByManagerOrMaster(
      User managerOrMaster, ReviewListRequestDto filterParams, Pageable pageable);
}
