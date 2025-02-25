package com.chone.server.domains.payment.domain;

import com.chone.server.commons.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
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
@Table(name = "pg_payment_log")
@Comment("PG사 결제 로그")
public class PgPaymentLog extends BaseEntity {
  @Id
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("로그 기본키")
  private UUID id;

  @NotNull
  @OneToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "payment_id", nullable = false, unique = true)
  @Comment("결제 ID")
  private Payment payment;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Comment("PG사 응답 상태(승인, 실패, 대기 등)")
  private PgStatus pgStatus;

  @NotNull
  @Column(nullable = false, length = 500)
  @Comment("PG사에서 받은 응답 메시지")
  private String responseMessage;

  @Column(length = 100)
  @Comment("PG사에서 발급한 거래 식별 번호 (PG에서 발급한 거래 ID), 결제 후 필수 값")
  private String pgTransactionId;

  public static PgPaymentLogBuilder builder(
      Payment payment, PgStatus pgStatus, String responseMessage) {

    return PgPaymentLog.innerBuilder()
        .payment(payment)
        .pgStatus(pgStatus)
        .responseMessage(responseMessage);
  }

  public void cancel() {
    this.pgStatus = PgStatus.CANCEL_SUCCESS;
  }
}
