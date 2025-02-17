package com.chone.server.domains.product.repository;

import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.product.repository.custom.ProductRepositoryCustom;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, ProductRepositoryCustom {

}