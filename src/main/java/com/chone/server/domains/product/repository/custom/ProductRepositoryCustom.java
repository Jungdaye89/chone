package com.chone.server.domains.product.repository.custom;

import com.chone.server.domains.product.domain.Product;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryCustom {

  Page<Product> searchProducts(UUID storeId, int page, int size, String sort,
      String direction, Double minPrice, Double maxPrice);
}