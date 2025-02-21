package com.chone.server.domains.review.infrastructure.jpa;

import com.chone.server.domains.review.domain.QReview;
import com.chone.server.domains.review.domain.Review;
import com.chone.server.domains.review.repository.ReviewRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<Review> findAllByUserId(Long userId) {

    QReview review = QReview.review;

    return queryFactory
        .selectFrom(review)
        .where(review.user.id.eq(userId), review.deletedAt.isNull())
        .fetch();
  }
}
