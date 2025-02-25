package com.chone.server.domains.review.repository;

import com.chone.server.domains.review.dto.response.ReviewDetailResponseDto;
import java.util.UUID;

public interface ReviewDetailSearchRepository {

  ReviewDetailResponseDto findReviewDetailById(UUID reviewId);
}
