package com.chone.server.domains.user.domain;

import static org.springframework.util.StringUtils.hasText;

import com.chone.server.commons.jpa.BaseEntity;
import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.review.domain.Review;
import com.chone.server.domains.store.domain.Store;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@Builder(access = AccessLevel.PUBLIC, builderMethodName = "innerBuilder")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_user")
@Comment("사용자")
public class User extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
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
  @Column(name = "password", nullable = false)
  @Comment("사용자 비밀번호")
  private String password;

  @NotNull
  @Column(name = "role", nullable = false)
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
  private List<Review> review = new ArrayList<>();

  public static UserBuilder builder(
      String username, String email, String hashedPassword, Role role, Boolean isAvailable) {
    return User.innerBuilder()
        .username(username)
        .email(email)
        .password(hashedPassword)
        .role(role)
        .isAvailable(isAvailable);
  }

  public void updateIsAvailable() {
    this.isAvailable = !this.isAvailable;
  }

  public void updateRole(Role role) {
    this.role = role;
  }

  public void updateUser(String email, String password) {
    if (hasText(email)) this.email = email;
    if (hasText(password)) this.password = password;
  }
}
