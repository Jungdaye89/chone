package com.chone.server.domains.store.repository.custom;

import com.chone.server.domains.store.domain.Store;
import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepositoryCustom {

  Page<Store> searchStores(int page, int size, String sort, String direction,
      LocalDate startDate, LocalDate endDate, String category, String sido, String sigungu,
      String dong, Long userId);
}