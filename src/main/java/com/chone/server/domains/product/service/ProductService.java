package com.chone.server.domains.product.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.product.dto.request.CreateRequestDto;
import com.chone.server.domains.product.dto.request.UpdateRequestDto;
import com.chone.server.domains.product.dto.response.CreateResponseDto;
import com.chone.server.domains.product.dto.response.PageInfoDto;
import com.chone.server.domains.product.dto.response.ReadResponseDto;
import com.chone.server.domains.product.dto.response.SearchResponseDto;
import com.chone.server.domains.product.exception.ProductExceptionCode;
import com.chone.server.domains.product.repository.ProductRepository;
import com.chone.server.domains.s3.service.S3Service;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.exception.StoreExceptionCode;
import com.chone.server.domains.store.service.StoreService;
import com.chone.server.domains.user.domain.User;
import com.chone.server.domains.user.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {

  private final ProductRepository productRepository;
  private final UserRepository userRepository;
  private final StoreService storeService;
  private final S3Service s3Service;

  public List<Product> findAllById(List<UUID> productIds) {

    return productRepository.findAllById(productIds);
  }

  public List<Product> findAllByStore(Store store) {

    return productRepository.findAllByStoreAndDeletedByIsNull(store);
  }

  @Transactional
  public CreateResponseDto createProduct(User user, CreateRequestDto createRequestDto,
      MultipartFile file) {

    Store store = storeService.findStoreById(createRequestDto.getStoreId());

    storeService.checkRoleWithStore(user, store);

    if (!file.isEmpty()) {
      createRequestDto.setImageUrl(s3Service.uploadFile(file));
    }

    Product product =
        productRepository.save(
            Product.builder(store)
                .name(createRequestDto.getName())
                .price(createRequestDto.getPrice())
                .imageUrl(createRequestDto.getImageUrl())
                .description(createRequestDto.getDescription())
                .build());

    return CreateResponseDto.from(product);
  }

  @Transactional(readOnly = true)
  public SearchResponseDto searchProducts(
      UUID storeId,
      int page,
      int size,
      String sort,
      String direction,
      Double minPrice,
      Double maxPrice) {

    Page<Product> products =
        productRepository.searchProducts(storeId, page, size, sort, direction, minPrice, maxPrice);

    return SearchResponseDto.builder()
        .content(
            products.getContent().stream().map(ReadResponseDto::from).collect(Collectors.toList()))
        .pageInfo(
            PageInfoDto.builder()
                .page(products.getNumber())
                .size(products.getSize())
                .totalElements(products.getTotalElements())
                .totalPages(products.getTotalPages())
                .build())
        .build();
  }

  @Transactional(readOnly = true)
  public ReadResponseDto getProduct(UUID storeId, UUID productId) {

    Product product = productRepository.findByStoreIdAndIdAndDeletedByIsNull(storeId, productId)
        .orElseThrow(() -> new ApiBusinessException(ProductExceptionCode.PRODUCT_NOT_FOUND));

    return ReadResponseDto.from(product);
  }

  @Transactional
  public void updateProduct(User user, UpdateRequestDto updateRequestDto, MultipartFile file,
      UUID productId) {

    Product product = findProductById(productId);

    storeService.checkRoleWithStore(user, product.getStore());

    if (!file.isEmpty()) {
      s3Service.removeFile(product.getImageUrl());
      updateRequestDto.setImageUrl(s3Service.uploadFile(file));
    } else {
      updateRequestDto.setImageUrl(product.getImageUrl());
    }

    product.update(updateRequestDto);
  }

  private Product findProductById(UUID id) {

    return productRepository
        .findById(id)
        .orElseThrow(() -> new ApiBusinessException(ProductExceptionCode.PRODUCT_NOT_FOUND));
  }

  @Transactional
  public void deleteProduct(User user, UUID productId) {

    Product product = findProductById(productId);

    storeService.checkRoleWithStore(user, product.getStore());

    deleteProduct(user, product);
  }

  public void deleteProduct(User user, Product product) {

    s3Service.removeFile(product.getImageUrl());

    product.delete(user);
  }

  private User findUserById(Long id) {

    return userRepository
        .findById(id)
        .orElseThrow(() -> new ApiBusinessException(StoreExceptionCode.USER_NOT_FOUND));
  }

  public List<Product> findAllByStore(Store store) {
    return productRepository.findAllByStoreAndDeletedByIsNull(store);
  }
}