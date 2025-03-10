package com.chone.server.domains.store.domain;

import com.chone.server.commons.jpa.BaseEntity;
import com.chone.server.domains.store.dto.request.UpdateRequestDto;
import com.chone.server.domains.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PUBLIC, builderMethodName = "innerBuilder")
@Comment("가게")
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_store")
public class Store extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @JdbcTypeCode(SqlTypes.UUID)
  private UUID id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  @Comment("가게 주인 고유번호")
  private User user;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "legal_dong_code_id", nullable = false)
  @Comment("지역 코드 아이디")
  private LegalDongCode legalDongCode;

  @OneToMany(mappedBy = "store")
  private List<StoreCategoryMap> storeCategoryMaps = new ArrayList<>();

  @NotNull
  @Column(nullable = false, length = 50)
  @Comment("가게명")
  private String name;

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

  @NotNull
  @Column(nullable = false, length = 150)
  @Comment("가게 상세주소")
  private String address;

  @NotNull
  @Column(nullable = false, length = 30)
  @Comment("가게 전화번호")
  private String phoneNumber;

  @Default
  @Comment("가게 오픈 여부")
  private Boolean isOpen = true;

  @Default
  @Comment("정보 공개 여부")
  private Boolean isPublic = true;

  public static StoreBuilder builder(User user, LegalDongCode legalDongCode) {

    return Store.innerBuilder()
        .user(user)
        .legalDongCode(legalDongCode);
  }

  public void update(UpdateRequestDto updateRequestDto, LegalDongCode legalDongCode) {

    this.legalDongCode = legalDongCode;
    this.name = updateRequestDto.getName();
    this.sido = updateRequestDto.getSido();
    this.sigungu = updateRequestDto.getSigungu();
    this.address = updateRequestDto.getAddress();
    this.phoneNumber = updateRequestDto.getPhoneNumber();
    this.isOpen = updateRequestDto.getIsOpen();
    this.isPublic = updateRequestDto.getIsPublic();
  }
}