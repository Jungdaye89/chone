package com.chone.server.domains.product.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.product.dto.request.CreateRequestDto;
import com.chone.server.domains.product.dto.request.UpdateRequestDto;
import com.chone.server.domains.product.dto.response.CreateResponseDto;
import com.chone.server.domains.product.dto.response.PageInfoDto;
import com.chone.server.domains.product.dto.response.ReadResponseDto;
import com.chone.server.domains.product.dto.response.SearchResponseDto;
import com.chone.server.domains.product.exception.ProductExceptionCode;
import com.chone.server.domains.product.repository.ProductRepository;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.exception.StoreExceptionCode;
import com.chone.server.domains.store.repository.StoreRepository;
import com.chone.server.domains.user.domain.User;
import com.chone.server.domains.user.repository.UserRepository;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final StoreRepository storeRepository;

  @Transactional
  public CreateResponseDto createProduct(CustomUserDetails userDetails,
      CreateRequestDto createRequestDto) {

    User user = findUserById(userDetails.getUser().getId());
    Store store = findStoreById(createRequestDto.getStoreId());

    checkRoleWithStore(user, store);

    Product product = productRepository.save(
        Product.builder(store)
            .name(createRequestDto.getName())
            .price(createRequestDto.getPrice())
            .imageUrl(createRequestDto.getImageUrl())
            .description(createRequestDto.getDescription())
            .build());

    return CreateResponseDto.from(product);
  }

  @Transactional(readOnly = true)
  public SearchResponseDto searchProducts(UUID storeId, int page, int size, String sort,
      String direction, Double minPrice, Double maxPrice) {

    Page<Product> products = productRepository.searchProducts(storeId, page, size, sort, direction,
        minPrice, maxPrice);

    return SearchResponseDto.builder()
        .content(
            products.getContent().stream()
                .map(ReadResponseDto::from)
                .collect(Collectors.toList())
        )
        .pageInfo(
            PageInfoDto.builder()
                .page(products.getNumber())
                .size(products.getSize())
                .totalElements(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .build()
        ).build();
  }

  @Transactional(readOnly = true)
  public ReadResponseDto getProduct(UUID productId) {

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new ApiBusinessException(
            ProductExceptionCode.PRODUCT_NOT_FOUND));

    return ReadResponseDto.from(product);
  }

  @Transactional
  public void updateProduct(CustomUserDetails userDetails, UpdateRequestDto updateRequestDto,
      UUID productId) {

    User user = findUserById(userDetails.getUser().getId());

    Product product = findProductById(productId);

    checkRoleWithStore(user, product.getStore());

    product.update(updateRequestDto);
  }

  private Product findProductById(UUID id) {

    return productRepository.findById(id)
        .orElseThrow(() -> new ApiBusinessException(ProductExceptionCode.PRODUCT_NOT_FOUND));
  }

  @Transactional
  public void deleteProduct(CustomUserDetails userDetails, UUID productId) {

    User user = findUserById(userDetails.getUser().getId());

    Product product = findProductById(productId);

    checkRoleWithStore(user, product.getStore());

    product.delete(user);
  }

  private User findUserById(Long id) {

    return userRepository.findById(id)
        .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.USER_NOT_FOUND));
  }

  private Store findStoreById(UUID id) {

    return storeRepository.findById(id)
        .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.STORE_NOT_FOUND));
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