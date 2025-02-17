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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {

  private final ProductService productService;

  @PostMapping
  @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
  public ResponseEntity<CreateResponseDto> createProduct(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody CreateRequestDto createRequestDto) {

    CreateResponseDto createResponseDto = productService.createProduct(userDetails,
        createRequestDto);

    return ResponseEntity.ok(createResponseDto);
  }

  @GetMapping("/{storeId}")
  public ResponseEntity<SearchResponseDto> searchProducts(
      @PathVariable UUID storeId,
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "sort", required = false, defaultValue = "created_at") String sort,
      @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
      @RequestParam(name = "minPrice", required = false) Double minPrice,
      @RequestParam(name = "maxPrice", required = false) Double maxPrice) {

    SearchResponseDto searchResponseDto = productService.searchProducts(storeId, page, size, sort,
        direction, minPrice, maxPrice);

    return ResponseEntity.ok(searchResponseDto);
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ReadResponseDto> getProduct(@PathVariable UUID productId) {

    ReadResponseDto readResponseDto = productService.getProduct(productId);

    return ResponseEntity.ok(readResponseDto);
  }

  @PutMapping("/{productId}")
  @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
  public ResponseEntity<Void> updateProduct(@AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody UpdateRequestDto updateRequestDto, @PathVariable UUID productId) {

    productService.updateProduct(userDetails, updateRequestDto, productId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping("/{productId}")
  @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
  public ResponseEntity<Void> deleteProduct(
      @AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable UUID productId) {

    productService.deleteProduct(userDetails, productId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}