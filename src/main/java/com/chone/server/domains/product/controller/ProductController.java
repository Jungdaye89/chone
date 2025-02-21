package com.chone.server.domains.product.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.product.dto.request.CreateRequestDto;
import com.chone.server.domains.product.dto.request.UpdateRequestDto;
import com.chone.server.domains.product.dto.response.CreateResponseDto;
import com.chone.server.domains.product.dto.response.ReadResponseDto;
import com.chone.server.domains.product.dto.response.SearchResponseDto;
import com.chone.server.domains.product.service.ProductService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

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

  @GetMapping("/{storeId}/{productId}")
  public ResponseEntity<ReadResponseDto> getProduct(@PathVariable("storeId") UUID storeId,
      @PathVariable("productId") UUID productId) {

    ReadResponseDto readResponseDto = productService.getProduct(storeId, productId);

    return ResponseEntity.ok(readResponseDto);
  }

  @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  @PreAuthorize("!hasRole('CUSTOMER')")
  public ResponseEntity<Void> updateProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestPart(value = "data") UpdateRequestDto updateRequestDto,
      @RequestPart(value = "file", required = false) MultipartFile file,
      @PathVariable("productId") UUID productId) {

    productService.updateProduct(userDetails.getUser(), updateRequestDto, file, productId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping("/{productId}")
  @PreAuthorize("!hasRole('CUSTOMER')")
  public ResponseEntity<Void> deleteProduct(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("productId") UUID productId) {

    productService.deleteProduct(userDetails.getUser(), productId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}