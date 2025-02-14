package com.chone.server.domains.payment.domain;

import com.chone.server.commons.jpa.BaseEntity;
import com.chone.server.domains.order.domain.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "p_payment")
@Comment("결제")
public class Payment extends BaseEntity {
  @Id
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("결제 기본키")
  private UUID id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "order_id", nullable = false)
  @Comment("주문")
  private Order order;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  @Comment("고객")
  private User user;

  @NotNull
  @Column(nullable = false, precision = 10, scale = 2)
  @Comment("결제 총 가격")
  private BigDecimal totalPrice = BigDecimal.ZERO;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Comment("결제 상태(대기, 승인, 완료, 취소요청, 취소, 실패)")
  private PaymentStatus status;

  @Column(length = 100)
  @Comment("결제 취소 사유")
  private String cancelReason;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Comment("결제 방식")
  private PaymentMethod paymentMethod;

  @NotNull
  @Column(length = 100, unique = true, nullable = false)
  @Comment("결제 회사(또는 PG사, 은행 등)에서 발급하는 거래 식별 번호")
  private String transactionId;

  public static PaymentBuilder builder(
      Order order,
      User user,
      BigDecimal totalPrice,
      PaymentStatus status,
      PaymentMethod paymentMethod,
      String transactionId) {

    return Payment.innerBuilder()
        .order(order)
        .user(user)
        .totalPrice(totalPrice)
        .status(status)
        .paymentMethod(paymentMethod)
        .transactionId(transactionId);
  }
}
