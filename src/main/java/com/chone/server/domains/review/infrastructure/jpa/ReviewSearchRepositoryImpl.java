package com.chone.server.domains.review.infrastructure.jpa;

import com.chone.server.domains.review.domain.QReview;
import com.chone.server.domains.review.dto.request.ReviewListRequestDto;
import com.chone.server.domains.review.dto.response.ReviewPageResponseDto;
import com.chone.server.domains.review.repository.ReviewSearchRepository;
import com.chone.server.domains.user.domain.User;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewSearchRepositoryImpl implements ReviewSearchRepository {

  private final JPAQueryFactory queryFactory;

  @Override
  public Page<ReviewPageResponseDto> findReviewsByCustomer(
      User customer, ReviewListRequestDto filterParams, Pageable pageable) {

    QReview review = QReview.review;
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(review.deletedAt.isNull());

    predicate.and(review.isPublic.isTrue().or(review.user.id.eq(customer.getId())));

    applyFilters(predicate, filterParams, review);

    return executeQuery(predicate, pageable, review);
  }

  @Override
  public Page<ReviewPageResponseDto> findReviewsByOwner(
      User owner, ReviewListRequestDto filterParams, Pageable pageable) {

    QReview review = QReview.review;
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(review.deletedAt.isNull());

    predicate.and(review.store.user.id.eq(owner.getId()).or(review.isPublic.isTrue()));

    applyFilters(predicate, filterParams, review);

    return executeQuery(predicate, pageable, review);
  }

  @Override
  public Page<ReviewPageResponseDto> findReviewsByManagerOrMaster(
      User user, ReviewListRequestDto filterParams, Pageable pageable) {

    QReview review = QReview.review;
    BooleanBuilder predicate = new BooleanBuilder();

    predicate.and(review.deletedAt.isNull());

    applyFilters(predicate, filterParams, review);

    return executeQuery(predicate, pageable, review);
  }

  private Page<ReviewPageResponseDto> executeQuery(
      BooleanBuilder predicate, Pageable pageable, QReview review) {

    List<OrderSpecifier<?>> orderSpecifiers = getSortOrders(review, pageable);

    List<ReviewPageResponseDto> content =
        queryFactory
            .selectFrom(review)
            .where(predicate)
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch()
            .stream()
            .map(ReviewPageResponseDto::from)
            .toList();

    long total = queryFactory.selectFrom(review).where(predicate).fetchCount();

    return new PageImpl<>(content, pageable, total);
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

  private List<OrderSpecifier<?>> getSortOrders(QReview review, Pageable pageable) {
    List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

    for (Sort.Order order : pageable.getSort()) {
      switch (order.getProperty()) {
        case "rating":
          orderSpecifiers.add(order.isAscending() ? review.rating.asc() : review.rating.desc());
          break;
        case "createdAt":
          orderSpecifiers.add(
              order.isAscending() ? review.createdAt.asc() : review.createdAt.desc());
          break;
        case "updatedAt":
          orderSpecifiers.add(
              order.isAscending() ? review.updatedAt.asc() : review.updatedAt.desc());
          break;
        default:
          orderSpecifiers.add(review.createdAt.desc());
      }
    }

    return orderSpecifiers;
  }
}
