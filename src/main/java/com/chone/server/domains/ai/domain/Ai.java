package com.chone.server.domains.ai.domain;

import com.chone.server.commons.jpa.BaseEntity;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.store.domain.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC, builderMethodName = "innerBuilder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_ai")
@Comment("AI 생성 테이블")
public class Ai extends BaseEntity {

  @Id
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("AI 기본키")
  private UUID id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false)
  @Comment("가게 외래키")
  private Store store;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id", nullable = false)
  @Comment("상품 외래키")
  private Product product;

  @NotNull
  @Column(name = "request_text", nullable = false, columnDefinition = "TEXT")
  @Comment("요청 텍스트")
  private String requestText;

  @NotNull
  @Column(name = "response_text", nullable = false, columnDefinition = "TEXT")
  @Comment("응답 텍스트")
  private String responseText;

  public static AiBuilder builder(
      Store store, Product product, String requestText, String responseText) {

    return Ai.innerBuilder()
        .store(store)
        .product(product)
        .requestText(requestText)
        .responseText(responseText);
  }
}
