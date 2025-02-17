package com.chone.server.domains.store.domain;

import com.chone.server.commons.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.util.ArrayList;
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
@Table(name = "p_category")
public class Category extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.UUID)
  @Comment("고유 번호")
  private UUID id;

  @OneToMany(mappedBy = "category")
  private List<StoreCategoryMap> storeCategoryMaps = new ArrayList<>();

  @NotNull
  @Column(nullable = false, length = 10)
  @Comment("이름")
  private String name;

  @Default
  @Comment("현재 사용 여부")
  private Boolean isAvailable = true;
}