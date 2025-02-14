package com.chone.server.domains.order.domain;

import com.chone.server.commons.jpa.BaseEntity;
import com.chone.server.domains.product.domain.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC, builderMethodName = "innerBuilder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_order_item")
@Comment("주문 상품")
public class OrderItem extends BaseEntity {
  @Id
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("주문 상품 기본키")
  private UUID id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  @Comment("주문")
  private Order order;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  @Comment("상품")
  private Product product;

  @NotNull
  @Column(nullable = false)
  @Comment("주문한 상품의 개수")
  private Long amount = 1L;

  @NotNull
  @Column(nullable = false, precision = 10, scale = 2)
  @Comment("주문한 상품의 총 금액")
  private BigDecimal price = BigDecimal.ZERO;

  public static OrderItemBuilder builder(
      Order order, Product product, Long amount, BigDecimal price) {

    return OrderItem.innerBuilder().order(order).product(product).amount(amount).price(price);
  }
}
