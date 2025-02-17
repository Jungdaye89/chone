package com.chone.server.domains.user.domain;

import com.chone.server.commons.jpa.BaseEntity;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.payment.domain.Payment;
import com.chone.server.domains.review.domain.entity.Review;
import com.chone.server.domains.store.domain.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Table(name = "p_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

  @NotNull
  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Comment("사용자 기본키")
  private Long id;

  @NotNull
  @Column(name = "username", nullable = false, unique = true, length = 10)
  @Comment("사용자 아이디")
  private String username;

  @NotNull
  @Column(name = "email", nullable = false, unique = true)
  @Comment("사용자 이메일")
  private String email;

  @NotNull
  @Column(name = "password", nullable = false, length = 15)
  @Comment("사용자 비밀번호")
  private String password;

  @NotNull
  @Column(name = "role", nullable = false, unique = true)
  @Enumerated(EnumType.STRING)
  @Comment("사용자 역할")
  private Role role;

  @NotNull
  @Column(name = "is_available", nullable = false, unique = false)
  @Comment("휴면상태 여부")
  private Boolean isAvailable;

  @OneToMany(mappedBy = "user")
  private List<Order> order = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Store> store = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Payment> payments = new ArrayList<>();

  @OneToMany(mappedBy = "user")
  private List<Review> review = new ArrayList<>();
}
