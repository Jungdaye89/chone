package com.chone.server.domains.payment.domain;

import static org.springframework.util.StringUtils.hasText;

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
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  @Comment("주문")
  private Order order;

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

  public static PaymentBuilder builder(
      Order order, PaymentStatus status, PaymentMethod paymentMethod) {

    return Payment.innerBuilder().order(order).status(status).paymentMethod(paymentMethod);
  }

  public void updateStatus(PaymentStatus status) {
    if (status != null) this.status = status;
  }

  public void updateCancelReason(String cancelReason) {
    if (!hasText(cancelReason)) this.cancelReason = cancelReason;
  }
}
