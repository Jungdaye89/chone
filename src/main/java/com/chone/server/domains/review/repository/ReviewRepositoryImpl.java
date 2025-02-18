package com.chone.server.domains.review.repository;

import com.chone.server.domains.review.domain.QReview;
import com.chone.server.domains.review.dto.request.ReviewListRequestDto;
import com.chone.server.domains.review.dto.response.ReviewPageResponseDto;
import com.chone.server.domains.user.domain.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewSearchRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<ReviewPageResponseDto> findReviewsByCustomer(
      User customer, ReviewListRequestDto filterParams, Pageable pageable) {
    QReview review = QReview.review;
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(review.user.id.eq(customer.getId()).or(review.isPublic.isTrue()));

    applyFilters(predicate, filterParams, review);

    return executeQuery(predicate, pageable);
  }

  @Override
  public Page<ReviewPageResponseDto> findReviewsByOwner(
      User owner, ReviewListRequestDto filterParams, Pageable pageable) {
    QReview review = QReview.review;
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(review.store.user.id.eq(owner.getId()).or(review.isPublic.isTrue()));

    applyFilters(predicate, filterParams, review);

    return executeQuery(predicate, pageable);
  }

  @Override
  public Page<ReviewPageResponseDto> findReviewsByManagerOrMaster(
      User user, ReviewListRequestDto filterParams, Pageable pageable) { // ✅ User 인자 추가!
    QReview review = QReview.review;
    BooleanBuilder predicate = new BooleanBuilder();

    applyFilters(predicate, filterParams, review);

    return executeQuery(predicate, pageable);
  }

  private void applyFilters(
      BooleanBuilder predicate, ReviewListRequestDto filterParams, QReview review) {
    if (filterParams.getStoreId() != null) {
      predicate.and(review.store.id.eq(filterParams.getStoreId()));
    }
    if (filterParams.getOrderId() != null) {
      predicate.and(review.order.id.eq(filterParams.getOrderId()));
    }
    if (filterParams.getMinRating() != null) {
      predicate.and(review.rating.goe(filterParams.getMinRating()));
    }
    if (filterParams.getMaxRating() != null) {
      predicate.and(review.rating.loe(filterParams.getMaxRating()));
    }
    if (filterParams.getHasImage() != null && filterParams.getHasImage()) {
      predicate.and(review.imageUrl.isNotNull());
    }
  }

  private Page<ReviewPageResponseDto> executeQuery(BooleanBuilder predicate, Pageable pageable) {

    List<ReviewPageResponseDto> content =
        queryFactory
            .selectFrom(QReview.review)
            .where(predicate)
            .orderBy(QReview.review.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch()
            .stream()
            .map(ReviewPageResponseDto::from)
            .toList();

    long total = queryFactory.selectFrom(QReview.review).where(predicate).fetchCount();

    return new PageImpl<>(content, pageable, total);
  }
}
