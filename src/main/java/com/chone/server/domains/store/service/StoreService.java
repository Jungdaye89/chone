package com.chone.server.domains.store.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.store.domain.Category;
import com.chone.server.domains.store.domain.LegalDongCode;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.domain.StoreCategoryMap;
import com.chone.server.domains.store.dto.request.CreateRequestDto;
import com.chone.server.domains.store.dto.request.UpdateRequestDto;
import com.chone.server.domains.store.dto.response.CreateResponseDto;
import com.chone.server.domains.store.dto.response.PageInfoDto;
import com.chone.server.domains.store.dto.response.ReadResponseDto;
import com.chone.server.domains.store.dto.response.SearchResponseDto;
import com.chone.server.domains.store.exception.StoreExceptionCode;
import com.chone.server.domains.store.repository.CategoryRepository;
import com.chone.server.domains.store.repository.LegalDongCodeRepository;
import com.chone.server.domains.store.repository.StoreCategoryMapRepository;
import com.chone.server.domains.store.repository.StoreRepository;
import com.chone.server.domains.user.domain.User;
import com.chone.server.domains.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {

  private final UserRepository userRepository;
  private final StoreRepository storeRepository;
  private final LegalDongCodeRepository legalDongCodeRepository;
  private final CategoryRepository categoryRepository;
  private final StoreCategoryMapRepository storeCategoryMapRepository;

  @Transactional
  public CreateResponseDto createStore(CreateRequestDto createRequestDto) {

    User user = userRepository.findById(createRequestDto.getUserId())
        .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.USER_NOT_FOUND));

    LegalDongCode legalDongCode = findLegalDongCodeBySidoAndSigunguAndDong(
        createRequestDto.getSido(), createRequestDto.getSigungu(), createRequestDto.getDong());

    Store store = storeRepository.save(
        Store.builder(user, legalDongCode)
            .name(createRequestDto.getName())
            .sido(createRequestDto.getSido())
            .sigungu(createRequestDto.getSigungu())
            .dong(createRequestDto.getDong())
            .address(createRequestDto.getAddress())
            .phoneNumber(createRequestDto.getPhoneNumber())
            .build());

    for (String categoryName : createRequestDto.getCategory()) {
      Category category = categoryRepository.findByName(categoryName)
          .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.CATEGORY_NOT_FOUND));

      storeCategoryMapRepository.save(new StoreCategoryMap(store, category));
    }

    return new CreateResponseDto(store);
  }

  @Transactional(readOnly = true)
  public SearchResponseDto searchStores(int page, int size, String sort, String direction,
      LocalDate startDate, LocalDate endDate, String category, String sido,
      String sigungu, String dong, Long userId) {

    Page<Store> stores = storeRepository.searchStores(page, size, sort, direction, startDate,
        endDate, category, sido, sigungu, dong, userId);

    return SearchResponseDto.builder()
        .content(
            stores.getContent().stream()
                .map(ReadResponseDto::from)
                .collect(Collectors.toList()))
        .pageInfo(
            PageInfoDto.builder()
                .page(stores.getNumber())
                .size(stores.getSize())
                .totalElements(stores.getTotalElements())
                .totalPages(stores.getTotalPages())
                .build())
        .build();
  }

  @Transactional(readOnly = true)
  public ReadResponseDto getStore(UUID storeId) {

    Store store = findStoreById(storeId);

    return ReadResponseDto.from(store);
  }

  @Transactional
  public void updateStore(UserDetails userDetails, UUID storeId,
      UpdateRequestDto updateRequestDto) {

    User user = findUserByUsername(userDetails.getUsername());

    Store store = findStoreById(storeId);

    LegalDongCode legalDongCode = findLegalDongCodeBySidoAndSigunguAndDong(
        updateRequestDto.getSido(), updateRequestDto.getSigungu(), updateRequestDto.getDong());

    checkRoleWithStore(user, store);

    store.update(updateRequestDto, legalDongCode);
    updateStoreCategoryMap(store, updateRequestDto.getCategory());
  }

  @Transactional
  public void deleteStore(UserDetails userDetails, UUID storeId) {

    User user = findUserByUsername(userDetails.getUsername());

    Store store = findStoreById(storeId);

    checkRoleWithStore(user, store);

    store.delete(user);
  }

  private User findUserByUsername(String username) {

    return userRepository.findByUsername(username)
        .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.USER_NOT_FOUND));
  }

  private Store findStoreById(UUID id) {

    return storeRepository.findById(id)
        .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.STORE_NOT_FOUND));
  }

  private LegalDongCode findLegalDongCodeBySidoAndSigunguAndDong(String sido, String sigungu,
      String dong) {

    return legalDongCodeRepository.findBySidoAndSigunguAndDong(sido, sigungu, dong)
        .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.LEGAL_DONG_NOT_FOUND));
  }

  private void updateStoreCategoryMap(Store store, List<String> categoryNames) {

    List<StoreCategoryMap> oldMaps = store.getStoreCategoryMaps();

    List<String> oldCategoryNames = oldMaps.stream()
        .map(e -> e.getCategory().getName())
        .collect(Collectors.toList());

    // 기존의 카테고리가 수정하려는 카테고리 목록에 없을 경우 삭제
    for (StoreCategoryMap map : oldMaps) {
      String oldName = map.getCategory().getName();
      if (!categoryNames.contains(oldName)) {
        storeCategoryMapRepository.delete(map);
      }
    }

    // 새로운 카테고리가 기존의 카테고리에 없을 경우 생성
    for (String name : categoryNames) {
      if (!oldCategoryNames.contains(name)) {
        Category category = categoryRepository.findByName(name)
            .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.CATEGORY_NOT_FOUND));

        StoreCategoryMap map = new StoreCategoryMap(store, category);
        storeCategoryMapRepository.save(map);
      }
    }
  }

  private void checkRoleWithStore(User user, Store store) {

    switch (user.getRole()) {
      case OWNER -> {
        if (!store.getUser().equals(user)) {
          throw new ApiBusinessException(StoreExceptionCode.USER_OWNED_STORE_NOT_FOUND);
        }
      }
      case MANAGER, MASTER -> {
      }
      default -> {
        throw new ApiBusinessException(StoreExceptionCode.USER_NO_AUTH);
      }
    }
  }
}