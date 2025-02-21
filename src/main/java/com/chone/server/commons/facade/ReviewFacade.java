package com.chone.server.commons.facade;

import com.chone.server.domains.review.dto.response.ReviewDetailResponseDto;
import java.util.List;

public interface ReviewFacade {

  List<ReviewDetailResponseDto> findReviewsByUserId(Long userId);
}
