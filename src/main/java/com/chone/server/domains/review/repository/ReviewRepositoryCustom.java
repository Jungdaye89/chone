package com.chone.server.domains.review.repository;

import com.chone.server.domains.review.domain.Review;
import java.util.List;

public interface ReviewRepositoryCustom {

  List<Review> findAllByUserId(Long userId);
}
