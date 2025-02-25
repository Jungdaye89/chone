package com.chone.server.commons.facade;

import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.User;
import java.util.List;
import java.util.UUID;

public interface StoreFacade {

  Store findStoreById(UUID id);
  List<Store> findAllStoreByUserId(Long id);
  void checkRoleWithStore(User user, Store store);
  void deleteStore(User user, Store store);
}