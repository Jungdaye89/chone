package com.chone.server.domains.store.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Table(name = "p_legal_dong_code")
public class LegalDongCode {

  @Id
  @Column(length = 10)
  @Comment("지역 코드")
  private String id;

  @NotNull
  @Column(nullable = false, length = 40)
  @Comment("시도명")
  private String sido;

  @Column(length = 40)
  @Comment("시군구명")
  private String sigungu;

  @Column(length = 40)
  @Comment("동명")
  private String dong;

  @Default
  @Comment("현재 사용 여부")
  private boolean isAvailable = true;
}