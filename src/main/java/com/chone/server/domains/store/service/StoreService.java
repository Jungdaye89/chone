package com.chone.server.domains.store.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.store.domain.Category;
import com.chone.server.domains.store.domain.LegalDongCode;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.domain.StoreCategoryMap;
import com.chone.server.domains.store.dto.request.CreateRequestDto;
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
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    LegalDongCode legalDongCode = legalDongCodeRepository.findBySidoAndSigunguAndDong(
            createRequestDto.getSido(), createRequestDto.getSigungu(), createRequestDto.getDong())
        .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.LEGAL_DONG_NOT_FOUND));

    Store store = storeRepository.save(
        Store.builder()
            .user(user)
            .legalDongCode(legalDongCode)
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

    Store store = storeRepository.findById(storeId)
        .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.STORE_NOT_FOUND));

    return ReadResponseDto.from(store);
  }
}