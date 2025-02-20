package com.chone.server.domains.review.repository;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

public interface ReviewStatisticsRepository {

  BigDecimal calculateAverageRating(UUID storeId);

  Map<Integer, Integer> countRatingDistribution(UUID storeId);

  int countTotalReviews(UUID storeId);
}
