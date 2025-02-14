package com.chone.server.domains.store.domain;

import com.chone.server.domains.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.List;
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
@Table(name = "p_store")
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.UUID)
  private UUID id;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @Comment("가게 주인 고유번호")
  private User user;

  @NotNull
  @ManyToOne
  @JoinColumn(name = "legal_dong_code_id", nullable = false)
  @Comment("지역 코드 아이디")
  private LegalDongCode legalDongCode;

  @OneToMany(mappedBy = "store")
  private List<StoreCategoryMap> storeCategoryMaps;

  @NotNull
  @Column(nullable = false, length = 50)
  @Comment("가게명")
  private String name;

  @NotNull
  @Column(nullable = false, length = 150)
  @Comment("가게 주소")
  private String address;

  @NotNull
  @Column(nullable = false, length = 30)
  @Comment("가게 전화번호")
  private String phoneNumber;

  @Default
  @Comment("가게 오픈 여부")
  private boolean isOpen = true;

  @Default
  @Comment("정보 공개 여부")
  private boolean isPublic = true;
}