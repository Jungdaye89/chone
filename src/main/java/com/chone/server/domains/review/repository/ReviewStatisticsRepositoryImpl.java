package com.chone.server.domains.review.repository;

import static com.chone.server.domains.review.domain.QReview.review;

import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewStatisticsRepositoryImpl implements ReviewStatisticsRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public BigDecimal calculateAverageRating(UUID storeId) {
    Double average =
        queryFactory
            .select(review.rating.avg())
            .from(review)
            .where(review.store.id.eq(storeId).and(review.deletedAt.isNull()))
            .fetchOne();

    return average != null
        ? BigDecimal.valueOf(average).setScale(2, RoundingMode.HALF_UP)
        : BigDecimal.ZERO;
  }

  @Override
  public Map<Integer, Integer> countRatingDistribution(UUID storeId) {
    Map<Integer, Integer> distribution = new HashMap<>();

    for (int i = 1; i <= 5; i++) {
      NumberExpression<Integer> ratingPath = review.rating.castToNum(Integer.class);
      int count =
          queryFactory
              .select(ratingPath.count().intValue())
              .from(review)
              .where(
                  review.store.id.eq(storeId).and(review.deletedAt.isNull()).and(ratingPath.eq(i)))
              .fetchOne();
      distribution.put(i, count);
    }
    return distribution;
  }

  @Override
  public int countTotalReviews(UUID storeId) {
    Integer count =
        queryFactory
            .select(review.id.count().intValue())
            .from(review)
            .where(review.store.id.eq(storeId).and(review.deletedAt.isNull()))
            .fetchOne();
    return count != null ? count : 0;
  }
}
