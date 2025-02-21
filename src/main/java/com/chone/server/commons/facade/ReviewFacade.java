package com.chone.server.commons.facade;

import com.chone.server.domains.review.dto.response.ReviewDetailResponseDto;
import java.util.List;
import java.util.UUID;

public interface ReviewFacade {

  List<ReviewDetailResponseDto> findReviewsByUserId(Long userId);

  ReviewDetailResponseDto findReviewDetailById(UUID reviewId);
}
