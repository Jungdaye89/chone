package com.chone.server.domains.store.facade;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.exception.GlobalExceptionCode;
import com.chone.server.commons.facade.ProductFacade;
import com.chone.server.commons.facade.StoreFacade;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.exception.StoreExceptionCode;
import com.chone.server.domains.store.repository.StoreRepository;
import com.chone.server.domains.user.domain.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StoreFacadeImpl implements StoreFacade {

  private final StoreRepository storeRepository;
  private final ProductFacade productFacade;

  @Override
  public Store findStoreById(UUID id) {

    return storeRepository
        .findByIdAndDeletedByIsNull(id)
        .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.STORE_NOT_FOUND));
  }

  @Override
  public List<Store> findAllStoreByUserId(Long id) {

    return storeRepository.findAllByUserIdAndDeletedByIsNull(id);
  }

  @Override
  public void checkRoleWithStore(User user, Store store) {

    switch (user.getRole()) {
      case OWNER -> {
        if (!store.getUser().equals(user)) {
          throw new ApiBusinessException(StoreExceptionCode.USER_OWNED_STORE_NOT_FOUND);
        }
      }
      case MANAGER, MASTER -> {
      }
      default -> {
        throw new ApiBusinessException(GlobalExceptionCode.UNAUTHORIZED);
      }
    }
  }

  @Override
  public void deleteStore(User user, Store store) {

    List<Product> products = productFacade.findAllByStore(store);
    products.forEach(p -> productFacade.deleteProduct(user, p));

    store.delete(user);
  }
}