package com.chone.server.domains.product.domain;

import com.chone.server.commons.jpa.BaseEntity;
import com.chone.server.domains.store.domain.Store;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_product")
public class Product extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("상품 고유번호")
  private UUID id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "store_id", nullable = false)
  @Comment("가게 ID")
  private Store store;

  @NotNull
  @Column(nullable = false)
  @Comment("상품명")
  private String name;

  @Comment("상품명")
  private String description;

  @NotNull
  @Column(nullable = false)
  @Comment("가격")
  private double price;

  @Comment("상품 사진")
  private String imageUrl;

  @Default
  @Comment("판매 가능 여부")
  private boolean isAvailable = true;
}