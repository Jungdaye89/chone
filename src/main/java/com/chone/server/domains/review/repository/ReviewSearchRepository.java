package com.chone.server.domains.review.repository;

import com.chone.server.domains.review.dto.request.ReviewListRequestDTO;
import com.chone.server.domains.review.dto.response.ReviewPageResponseDTO;
import com.chone.server.domains.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReviewSearchRepository {
  Page<ReviewPageResponseDTO> findReviewsByCustomer(
      User customer, ReviewListRequestDTO filterParams, Pageable pageable);

  Page<ReviewPageResponseDTO> findReviewsByOwner(
      User owner, ReviewListRequestDTO filterParams, Pageable pageable);

  Page<ReviewPageResponseDTO> findReviewsByManagerOrMaster(
      User managerOrMaster, ReviewListRequestDTO filterParams, Pageable pageable);
}
