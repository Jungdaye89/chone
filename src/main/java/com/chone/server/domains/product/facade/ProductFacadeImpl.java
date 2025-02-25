package com.chone.server.domains.product.facade;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.ProductFacade;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.product.exception.ProductExceptionCode;
import com.chone.server.domains.product.repository.ProductRepository;
import com.chone.server.domains.s3.service.S3Service;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductFacadeImpl implements ProductFacade {

  private final ProductRepository productRepository;
  private final S3Service s3Service;

  @Override
  public List<Product> findAllById(List<UUID> productIds) {

    return productRepository.findAllById(productIds);
  }

  @Override
  public List<Product> findAllByStore(Store store) {

    return productRepository.findAllByStoreAndDeletedByIsNull(store);
  }

  @Override
  public Product findProductById(UUID id) {

    return productRepository
        .findById(id)
        .orElseThrow(() -> new ApiBusinessException(ProductExceptionCode.PRODUCT_NOT_FOUND));
  }

  @Override
  public void deleteProduct(User user, Product product) {

    s3Service.removeFile(product.getImageUrl());

    product.delete(user);
  }

  @Override
  public Product findByIdAndStoreUser(UUID productId, User user) {
    return productRepository
        .findByIdAndStoreUser(productId, user)
        .orElseThrow(() -> new ApiBusinessException(ProductExceptionCode.PRODUCT_NOT_FOUND));
  }

  @Override
  public void updateDescription(Product product, String description) {
    product.updateDescription(description);
  }
}
