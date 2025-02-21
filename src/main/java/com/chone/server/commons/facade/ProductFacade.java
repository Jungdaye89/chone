package com.chone.server.commons.facade;

import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.User;
import java.util.List;
import java.util.UUID;

public interface ProductFacade {

  List<Product> findAllById(List<UUID> productIds);
  List<Product> findAllByStore(Store store);
  Product findProductById(UUID id);
  void deleteProduct(User user, Product product);
}