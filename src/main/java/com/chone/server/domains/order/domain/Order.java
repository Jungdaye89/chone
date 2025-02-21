package com.chone.server.domains.order.domain;

import com.chone.server.commons.jpa.BaseEntity;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC, builderMethodName = "innerBuilder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_order")
@Comment("주문")
public class Order extends BaseEntity {
  @Id
  @UuidGenerator
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("주문 기본키")
  private UUID id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "store_id", nullable = false)
  @Comment("가게")
  private Store store;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "customer_id", nullable = false)
  @Comment("고객")
  private User user;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Comment("주문 유형")
  private OrderType orderType;

  @NotNull
  @Column(nullable = false, precision = 10, scale = 2)
  @Comment("총 가격")
  @Builder.Default
  private int totalPrice = 0;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Comment("주문 상태(대기, 승인, 완료, 취소)")
  private OrderStatus status;

  @Enumerated(EnumType.STRING)
  @Column
  @Comment("주문 취소 사유")
  private OrderCancelReason cancelReason;

  @NotNull
  @ColumnDefault("true")
  @Column(nullable = false)
  @Comment("취소 가능 여부")
  private boolean isCancelable;

  @Column(length = 150)
  @Comment("주문 요청 사항")
  private String request;

  @Column(length = 30)
  @Comment("배송지")
  private String address;

  @Builder.Default
  @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<OrderItem> orderItems = new ArrayList<>();

  public static OrderBuilder builder(
      Store store, User user, OrderType orderType, int totalPrice, OrderStatus status) {

    return Order.innerBuilder()
        .store(store)
        .user(user)
        .isCancelable(true)
        .orderType(orderType)
        .totalPrice(totalPrice)
        .status(status);
  }

  public void addOrderItem(List<OrderItem> items) {
    items.forEach(
        item -> {
          this.orderItems.add(item);
          item.setOrder(this);
        });
    this.orderItems = items;
  }

  public void cancel(OrderCancelReason cancelReason) {
    this.status = OrderStatus.CANCELED;
    this.cancelReason = cancelReason != null ? cancelReason : OrderCancelReason.OTHER;
    this.isCancelable = false;
  }

  public void updateNotCancelable() {
    this.isCancelable = false;
  }

  public void softDelete(User user) {
    this.delete(user);
    this.orderItems.forEach(item -> item.delete(user));
  }

  public void updateStatus(OrderStatus status) {
    if (status != null) this.status = status;
  }
}
