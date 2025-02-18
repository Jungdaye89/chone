package com.chone.server.domains.review.repository;

import com.chone.server.domains.review.domain.QReview;
import com.chone.server.domains.review.dto.request.ReviewListRequestDTO;
import com.chone.server.domains.review.dto.response.ReviewPageResponseDTO;
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
  public Page<ReviewPageResponseDTO> findReviewsByCustomer(
      User customer, ReviewListRequestDTO filterParams, Pageable pageable) {
    QReview review = QReview.review;
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(review.user.id.eq(customer.getId()).or(review.isPublic.isTrue()));

    applyFilters(predicate, filterParams, review);

    return executeQuery(predicate, pageable);
  }

  @Override
  public Page<ReviewPageResponseDTO> findReviewsByOwner(
      User owner, ReviewListRequestDTO filterParams, Pageable pageable) {
    QReview review = QReview.review;
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(review.store.user.id.eq(owner.getId()).or(review.isPublic.isTrue()));

    applyFilters(predicate, filterParams, review);

    return executeQuery(predicate, pageable);
  }

  @Override
  public Page<ReviewPageResponseDTO> findReviewsByManagerOrMaster(
      User user, ReviewListRequestDTO filterParams, Pageable pageable) { // ✅ User 인자 추가!
    QReview review = QReview.review;
    BooleanBuilder predicate = new BooleanBuilder();

    applyFilters(predicate, filterParams, review);

    return executeQuery(predicate, pageable);
  }

  private void applyFilters(
      BooleanBuilder predicate, ReviewListRequestDTO filterParams, QReview review) {
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

  private Page<ReviewPageResponseDTO> executeQuery(BooleanBuilder predicate, Pageable pageable) {

    List<ReviewPageResponseDTO> content =
        queryFactory
            .selectFrom(QReview.review)
            .where(predicate)
            .orderBy(QReview.review.createdAt.desc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch()
            .stream()
            .map(ReviewPageResponseDTO::from)
            .toList();

    long total = queryFactory.selectFrom(QReview.review).where(predicate).fetchCount();

    return new PageImpl<>(content, pageable, total);
  }
}
