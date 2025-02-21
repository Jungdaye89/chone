package com.chone.server.domains.review.repository;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.review.domain.QReview;
import com.chone.server.domains.review.domain.Review;
import com.chone.server.domains.review.dto.response.ReviewDetailResponseDto;
import com.chone.server.domains.review.exception.ReviewExceptionCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewDetailRepositoryImpl implements ReviewDetailSearchRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public ReviewDetailResponseDto findReviewDetailById(UUID reviewID) {

    QReview review = QReview.review;

    Review result =
        queryFactory
            .selectFrom(review)
            .join(review.store)
            .fetchJoin()
            .join(review.user)
            .fetchJoin()
            .where(review.id.eq(reviewID), review.deletedAt.isNull())
            .fetchOne();

    if (result == null) {
      throw new ApiBusinessException(ReviewExceptionCode.REVIEW_NOT_FOUND);
    }

    return ReviewDetailResponseDto.from(result);
  }
}
