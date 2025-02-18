package com.chone.server.domains.review.repository;

import com.chone.server.domains.review.dto.request.ReviewListRequestDTO;
import com.chone.server.domains.review.dto.response.ReviewPageResponse;
import com.chone.server.domains.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewSearchRepository {
  Page<ReviewPageResponse> findReviewsByCustomer(
      User customer, ReviewListRequestDTO filterParams, Pageable pageable);

  Page<ReviewPageResponse> findReviewsByOwner(
      User owner, ReviewListRequestDTO filterParams, Pageable pageable);

  Page<ReviewPageResponse> findReviewsByManagerOrMaster(
      User managerOrMaster, ReviewListRequestDTO filterParams, Pageable pageable);
}
