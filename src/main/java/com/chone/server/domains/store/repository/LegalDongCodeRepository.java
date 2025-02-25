package com.chone.server.domains.store.repository;

import com.chone.server.domains.store.domain.LegalDongCode;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalDongCodeRepository extends JpaRepository<LegalDongCode, String> {

  Optional<LegalDongCode> findBySidoAndSigunguAndDongAndIsAvailableIsTrue(String sido,
      String sigungu,
      String dong);
}