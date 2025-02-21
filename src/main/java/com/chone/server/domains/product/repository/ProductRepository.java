package com.chone.server.domains.product.repository;

import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.product.repository.custom.ProductRepositoryCustom;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {

  Optional<Product> findByIdAndStoreUser(UUID id, User user);

  Optional<Product> findByStoreIdAndIdAndDeletedByIsNull(UUID storeId, UUID id);

  List<Product> findAllByStoreAndDeletedByIsNull(Store store);
}