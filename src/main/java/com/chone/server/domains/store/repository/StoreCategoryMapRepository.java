package com.chone.server.domains.store.repository;

import com.chone.server.domains.store.domain.StoreCategoryMap;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreCategoryMapRepository extends JpaRepository<StoreCategoryMap, UUID> {

}