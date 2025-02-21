package com.chone.server.domains.store.repository;

import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.repository.custom.StoreRepositoryCustom;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, UUID>, StoreRepositoryCustom {

  Optional<Store> findByIdAndDeletedByIsNull(UUID id);

  List<Store> findAllByUserIdAndDeletedByIsNull(Long id);
}