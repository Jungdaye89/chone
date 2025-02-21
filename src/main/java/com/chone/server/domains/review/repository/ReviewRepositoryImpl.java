package com.chone.server.domains.review.repository;

import com.chone.server.domains.review.domain.QReview;
import com.chone.server.domains.review.domain.Review;
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
