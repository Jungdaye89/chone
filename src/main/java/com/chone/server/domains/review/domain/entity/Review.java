package com.chone.server.domains.review.domain.entity;

import com.chone.server.commons.jpa.BaseEntity;
import com.chone.server.domains.store.domain.entity.Store;
import com.chone.server.domains.user.domain.entity.User;
import com.chone.server.domains.order.domain.entity.Order;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC, builderMethodName = "innerBuilder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_review")
@Comment("리뷰 테이블")
public class Review extends BaseEntity {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.UUID)
    @Comment("리뷰 기본키")
    private UUID id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "order_id", nullable = false)
    @Comment("주문 외래키")
    private Order order;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "store_id", nullable = false)
    @Comment("가게 외래키")
    private Store store;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @Comment("사용자 외래키")
    private User user;

    @NotNull
    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    @Comment("리뷰 내용")
    private String content;

    @Column(name = "image_url", length = 255, unique = true)
    @Comment("상품 사진")
    private String imageUrl;

    @NotNull
    @Column(name = "rating", nullable = false, precision = 10, scale = 2)
    @Comment("1~5점 범위의 평점")
    private BigDecimal rating;

    @NotNull
    @Column(name = "is_public", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Comment("사용자 정보 공개 여부")
    private Boolean isPublic;

    @Column(name = "deleted_reason", length = 150)
    @Comment("삭제 이유")
    private String deletedReason;

    public static ReviewBuilder builder(
            Order order, Store store, User user, String content, BigDecimal rating, Boolean isPublic) {
        return Review.innerBuilder()
                .order(order)
                .store(store)
                .user(user)
                .content(content)
                .rating(rating)
                .isPublic(isPublic);
    }
}
