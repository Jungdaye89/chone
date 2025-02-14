package com.chone.server.domains.store.repository.custom;

import com.chone.server.domains.store.domain.QCategory;
import com.chone.server.domains.store.domain.QStore;
import com.chone.server.domains.store.domain.QStoreCategoryMap;
import com.chone.server.domains.store.domain.Store;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryCustomImpl implements StoreRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  QStore qStore = QStore.store;
  QCategory qCategory = QCategory.category;
  QStoreCategoryMap qStoreCategoryMap = QStoreCategoryMap.storeCategoryMap;

  @Override
  public Page<Store> searchStores(int page, int size, String sort, String direction,
      LocalDate startDate, LocalDate endDate, String category, String sido, String sigungu,
      String dong, Long userId) {

    BooleanBuilder builder = new BooleanBuilder();

    if (startDate != null) {
      builder.and(qStore.createdAt.goe(startDate.atStartOfDay()));
    }

    if (endDate != null) {
      builder.and(qStore.createdAt.loe(endDate.plusDays(1).atStartOfDay()));
    }

    if (sido != null) {
      builder.and(qStore.sido.eq(sido));
    }

    if (sigungu != null) {
      builder.and(qStore.sigungu.eq(sigungu));
    }

    if (dong != null) {
      builder.and(qStore.dong.eq(dong));
    }

    if (userId != null) {
      builder.and(qStore.user.id.eq(userId));
    }

    PathBuilder<Store> storePath = new PathBuilder<>(Store.class, "store");
    Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;
    Expression<LocalDate> sortExpression = storePath.getComparable(sort, LocalDate.class);
    OrderSpecifier<?> orderSpecifier = new OrderSpecifier<>(order, sortExpression);

    List<Store> content = jpaQueryFactory
        .selectFrom(qStore)
        .leftJoin(qStore.storeCategoryMaps, qStoreCategoryMap).fetchJoin()
        .leftJoin(qStoreCategoryMap.category, qCategory).fetchJoin()
        .where(builder)
        .orderBy(orderSpecifier)
        .offset((long) page * size)
        .limit(size)
        .fetch();

    Long total = jpaQueryFactory
        .select(qStore.count())
        .from(qStore)
        .leftJoin(qStore.storeCategoryMaps, qStoreCategoryMap)
        .leftJoin(qStoreCategoryMap.category, qCategory)
        .where(builder)
        .fetchOne();

    PageRequest pageRequest = PageRequest.of(
        page,
        size,
        direction.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC,
        sort
    );

    return new PageImpl<>(content, pageRequest, total != null ? total : 0);
  }
}