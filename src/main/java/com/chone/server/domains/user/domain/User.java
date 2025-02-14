package com.chone.server.domains.user.domain;

import com.chone.server.commons.jpa.BaseEntity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.Order;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter
@Table(name = "p_user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @NotNull
    @Column(name = "id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    @Column(name="password", nullable = false, length = 15)
    @Comment("사용자 비밀번호")
    private String password;

    @NotNull
    @Column(name = "role", nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    @Comment("사용자 역할")
    private String role;

    @NotNull
    @Column(name = "is_available", nullable = false, unique = false)
    @Comment("휴면상태 여부")
    private Boolean isAvailable;

//    @OneToMany(mappedBy = "user")
//    private List<Order> order = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<Store> store = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<Payments> payments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user")
//    private List<Review> review = new ArrayList<>();




}
