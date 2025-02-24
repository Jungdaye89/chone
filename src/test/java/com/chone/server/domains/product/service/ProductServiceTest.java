package com.chone.server.domains.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.ProductFacade;
import com.chone.server.commons.facade.StoreFacade;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.product.dto.request.CreateRequestDto;
import com.chone.server.domains.product.dto.response.CreateResponseDto;
import com.chone.server.domains.product.repository.ProductRepository;
import com.chone.server.domains.s3.service.S3Service;
import com.chone.server.domains.store.domain.LegalDongCode;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.store.exception.StoreExceptionCode;
import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

  @Mock
  ProductRepository productRepository;

  @Mock
  S3Service s3Service;

  @Mock
  ProductFacade productFacade;

  @Mock
  StoreFacade storeFacade;

  @InjectMocks
  ProductService productService;

  @Nested
  @DisplayName("상품 생성 테스트")
  class CreateTest {

    @Test
    @DisplayName("파일 없음")
    void test1() {
      // given
      User user = User.builder("testUser",
              "testEmail",
              "testPassword",
              Role.OWNER,
              true)
          .build();

      Store store = Store.builder(user, new LegalDongCode())
          .build();

      UUID storeId = UUID.randomUUID();

      CreateRequestDto createRequestDto = CreateRequestDto.builder()
          .name("testProduct")
          .storeId(storeId)
          .build();

      given(storeFacade.findStoreById(storeId))
          .willReturn(store);
      given(productRepository.save(any(Product.class)))
          .willAnswer(invocation -> invocation.getArgument(0));

      // when
      CreateResponseDto result = productService.createProduct(user, createRequestDto, null);

      // then
      assertEquals("testProduct", result.getName());
    }

    @Test
    @DisplayName("파일 있음")
    void test2() {
      // given
      User user = User.builder("testUser",
              "testEmail",
              "testPassword",
              Role.OWNER,
              true)
          .build();

      Store store = Store.builder(user, new LegalDongCode())
          .build();

      UUID storeId = UUID.randomUUID();

      CreateRequestDto createRequestDto = CreateRequestDto.builder()
          .name("testProduct")
          .storeId(storeId)
          .build();

      MockMultipartFile file = new MockMultipartFile(
          "file",
          "testImage.png",
          "image/png",
          "random-image-binary-content".getBytes()
      );

      given(storeFacade.findStoreById(storeId)).willReturn(store);
      given(s3Service.uploadFile(file)).willReturn("mockedImageUrl");
      given(productRepository.save(any(Product.class)))
          .willAnswer(invocation -> invocation.getArgument(0));

      // when
      CreateResponseDto result = productService.createProduct(user, createRequestDto, file);

      // then
      assertEquals("testProduct", result.getName());
      assertEquals("mockedImageUrl", result.getImageUrl());
    }

    @Test
    @DisplayName("실패 - 가게 없음")
    void test3() {
      // given
      User user = User.builder("testUser",
              "testEmail",
              "testPassword",
              Role.OWNER,
              true)
          .build();

      Store store = Store.builder(user, new LegalDongCode())
          .build();

      UUID storeId = UUID.randomUUID();

      CreateRequestDto createRequestDto = CreateRequestDto.builder()
          .name("testProduct")
          .storeId(storeId)
          .build();

      // when
      given(storeFacade.findStoreById(storeId)).willThrow(
          new ApiBusinessException(StoreExceptionCode.STORE_NOT_FOUND));

      ApiBusinessException exception = assertThrows(
          ApiBusinessException.class,
          () -> productService.createProduct(user, createRequestDto, null));

      // then
      assertEquals(StoreExceptionCode.STORE_NOT_FOUND, exception.getExceptionCode());
    }

    @Test
    @DisplayName("실패 - 점주 아님")
    void test4() {
      // given
      User user1 = User.builder("testUser1",
              "testEmail",
              "testPassword",
              Role.OWNER,
              true)
          .build();

      User user2 = User.builder("testUser2",
              "testEmail",
              "testPassword",
              Role.OWNER,
              true)
          .build();

      Store store = Store.builder(user2, new LegalDongCode())
          .build();

      UUID storeId = UUID.randomUUID();

      CreateRequestDto createRequestDto = CreateRequestDto.builder()
          .name("testProduct")
          .storeId(storeId)
          .build();

      // when
      given(storeFacade.findStoreById(storeId)).willReturn(store);
      willThrow(new ApiBusinessException(StoreExceptionCode.USER_OWNED_STORE_NOT_FOUND))
          .given(storeFacade)
          .checkRoleWithStore(user1, store);
      ApiBusinessException exception = assertThrows(ApiBusinessException.class,
          () -> productService.createProduct(user1, createRequestDto, null));

      // then
      assertEquals(StoreExceptionCode.USER_OWNED_STORE_NOT_FOUND, exception.getExceptionCode());
    }
  }

  @Nested
  @DisplayName("상품 조회 테스트")
  class ReadTest {

  }

  @Nested
  @DisplayName("상품 수정 테스트")
  class UpdateTest {

  }

  @Nested
  @DisplayName("상품 삭제 테스트")
  class DeleteTest {

  }
}