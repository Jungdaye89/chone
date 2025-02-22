package com.chone.server.domains.product.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.product.dto.request.CreateRequestDto;
import com.chone.server.domains.product.dto.request.UpdateRequestDto;
import com.chone.server.domains.product.dto.response.CreateResponseDto;
import com.chone.server.domains.product.dto.response.ReadResponseDto;
import com.chone.server.domains.product.dto.response.SearchResponseDto;
import com.chone.server.domains.product.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "상품", description = "상품 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @Operation(summary = "상품 등록 API", description = "\n\n OWNER, MANAGER, MASTER 계정만 상품을 등록할 수 있습니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "ALL",
          description = "성공",
          content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = com.chone.server.domains.store.dto.response.SearchResponseDto.class),
              examples = {
                  @ExampleObject(name = "상품 등록 성공",
                      description = "상품 등록 성공 시 다음과 같은 응답데이터를 받습니다.",
                      value =
                          """
                              {
                                  "storeId": "c513edd6-6ecf-4504-949a-20c5b96956d8",
                                  "name": "라면",
                                  "price": 3000.0,
                                  "imageUrl": null,
                                  "description": "라면입니다."
                              }
                              """
                  )}))})
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("!hasRole('CUSTOMER')")
  public ResponseEntity<CreateResponseDto> createProduct(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestPart("data") CreateRequestDto createRequestDto,
      @RequestPart(value = "file", required = false) MultipartFile file) {

    CreateResponseDto createResponseDto = productService.createProduct(userDetails.getUser(),
        createRequestDto, file);

    return ResponseEntity.ok(createResponseDto);
  }

  @Operation(summary = "상품 검색 API", description = "\n\n 해당 가게의 상품을 검색합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "ALL",
          description = "성공",
          content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = com.chone.server.domains.store.dto.response.SearchResponseDto.class),
              examples = {
                  @ExampleObject(name = "상품 검색 성공",
                      description = "상품 검색 성공 시 다음과 같은 응답데이터를 받습니다.",
                      value =
                          """
                              {
                                  "content": [
                                      {
                                          "productId": "d5885bb6-113c-42ca-806a-e26238e62f61",
                                          "name": "라면",
                                          "description": "라면입니다.",
                                          "price": 3000.0,
                                          "imageUrl": null,
                                          "isAvailable": true
                                      }
                                  ],
                                  "pageInfo": {
                                      "page": 0,
                                      "size": 10,
                                      "totalElements": 1,
                                      "totalPages": 1
                                  }
                              }
                              """
                  )}))})
  @GetMapping("/{storeId}")
  public ResponseEntity<SearchResponseDto> searchProducts(
      @PathVariable("storeId") UUID storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "sort", required = false, defaultValue = "price") String sort,
      @RequestParam(name = "direction", required = false, defaultValue = "asc") String direction,
      @RequestParam(name = "minPrice", required = false) Double minPrice,
      @RequestParam(name = "maxPrice", required = false) Double maxPrice) {

    SearchResponseDto searchResponseDto = productService.searchProducts(storeId, page, size, sort,
        direction, minPrice, maxPrice);

    return ResponseEntity.ok(searchResponseDto);
  }

  @Operation(summary = "상품 상세 조회 API", description = "\n\n 해당 가게의 한 상품을 조회합니다.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "ALL",
          description = "성공",
          content =
          @Content(
              mediaType = "application/json",
              schema = @Schema(implementation = com.chone.server.domains.store.dto.response.SearchResponseDto.class),
              examples = {
                  @ExampleObject(name = "상품 조회 성공",
                      description = "상품 조회 성공 시 다음과 같은 응답데이터를 받습니다.",
                      value =
                          """
                              {
                                  "productId": "d5885bb6-113c-42ca-806a-e26238e62f61",
                                  "name": "라면",
                                  "description": "라면입니다.",
                                  "price": 3000.0,
                                  "imageUrl": null,
                                  "isAvailable": true
                              }
                              """
                  )}))})
  @GetMapping("/{storeId}/{productId}")
  public ResponseEntity<ReadResponseDto> getProduct(@PathVariable("storeId") UUID storeId,
      @PathVariable("productId") UUID productId) {

    ReadResponseDto readResponseDto = productService.getProduct(storeId, productId);

    return ResponseEntity.ok(readResponseDto);
  }

  @Operation(summary = "상품 수정 API", description = "\n\n OWNER, MANAGER, MASTER 계정만 상품을 수정합니다.")
  @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("!hasRole('CUSTOMER')")
  public ResponseEntity<Void> updateProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestPart(value = "data") UpdateRequestDto updateRequestDto,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @PathVariable("productId") UUID productId) {

    productService.updateProduct(userDetails.getUser(), updateRequestDto, file, productId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "상품 삭제 API", description = "\n\n OWNER, MANAGER, MASTER 계정만 상품을 삭제합니다.")
  @DeleteMapping("/{productId}")
  @PreAuthorize("!hasRole('CUSTOMER')")
  public ResponseEntity<Void> deleteProduct(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("productId") UUID productId) {

    productService.deleteProduct(userDetails.getUser(), productId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}