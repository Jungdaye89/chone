package com.chone.server.domains.product.repository.custom;

import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.product.domain.QProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

  private final JPAQueryFactory jpaQueryFactory;

  QProduct product = QProduct.product;

  @Override
  public Page<Product> searchProducts(UUID storeId, int page, int size, String sort,
      String direction, Double minPrice, Double maxPrice) {

    BooleanBuilder builder = new BooleanBuilder();

    if (storeId != null) {
      builder.and(product.store.id.eq(storeId));
    }

    if (minPrice != null) {
      builder.and(product.price.goe(minPrice));
    }

    if (maxPrice != null) {
      builder.and(product.price.loe(maxPrice));
    }

    Order order = direction.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;
    OrderSpecifier<?> orderSpecifier;
    switch (sort) {
      case "price" -> {
        orderSpecifier = (order == Order.ASC) ? product.price.asc() : product.price.desc();
      }
      default -> {
        orderSpecifier = (order == Order.ASC) ? product.createdAt.asc() : product.createdAt.desc();
      }
    }

    long offset = (long) page * size;

    List<Product> content = jpaQueryFactory
        .selectFrom(product)
        .where(builder)
        .orderBy(orderSpecifier)
        .offset(offset)
        .limit(size)
        .fetch();

    long total = jpaQueryFactory
        .select(product.count())
        .from(product)
        .where(builder)
        .fetchOne();

    Pageable pageable = PageRequest.of(
        page,
        size,
        direction.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC,
        sort);

    return new PageImpl<>(content, pageable, total);
  }
}