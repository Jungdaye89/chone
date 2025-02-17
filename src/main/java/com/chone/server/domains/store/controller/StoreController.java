package com.chone.server.domains.store.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.store.dto.request.CreateRequestDto;
import com.chone.server.domains.store.dto.request.UpdateRequestDto;
import com.chone.server.domains.store.dto.response.CreateResponseDto;
import com.chone.server.domains.store.dto.response.ReadResponseDto;
import com.chone.server.domains.store.dto.response.SearchResponseDto;
import com.chone.server.domains.store.service.StoreService;
import java.time.LocalDate;
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
@RequestMapping("/api/v1/stores")
public class StoreController {

  private final StoreService storeService;

  @PostMapping
  @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
  public ResponseEntity<CreateResponseDto> createStore(
      @AuthenticationPrincipal CustomUserDetails userDetails,
      @RequestBody CreateRequestDto createRequestDto) {

    CreateResponseDto createResponseDto = storeService.createStore(userDetails, createRequestDto);

    return ResponseEntity.ok(createResponseDto);
  }

  @GetMapping
  public ResponseEntity<SearchResponseDto> searchStores(
      @RequestParam(name = "page", required = false, defaultValue = "0") int page,
      @RequestParam(name = "size", required = false, defaultValue = "10") int size,
      @RequestParam(name = "sort", required = false, defaultValue = "createdAt") String sort,
      @RequestParam(name = "direction", required = false, defaultValue = "desc") String direction,
      @RequestParam(name = "startDate", required = false, defaultValue = "1970-01-01") LocalDate startDate,
      @RequestParam(name = "endDate", required = false, defaultValue = "9999-01-01") LocalDate endDate,
      @RequestParam(name = "category", required = false) String category,
      @RequestParam(name = "sido", required = false) String sido,
      @RequestParam(name = "sigungu", required = false) String sigungu,
      @RequestParam(name = "dong", required = false) String dong,
      @RequestParam(name = "userId", required = false) Long userId) {

    if (startDate == null) {
      startDate = LocalDate.now();
    }

    SearchResponseDto searchResponseDto = storeService.searchStores(page, size, sort, direction,
        startDate, endDate, category, sido, sigungu, dong, userId);

    return ResponseEntity.ok(searchResponseDto);
  }

  @GetMapping("/{storeId}")
  public ResponseEntity<ReadResponseDto> getStore(@PathVariable("storeId") UUID storeId) {

    ReadResponseDto readResponseDto = storeService.getStore(storeId);

    return ResponseEntity.ok(readResponseDto);
  }

  @PutMapping("/{storeId}")
  public ResponseEntity<Void> updateStore(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable UUID storeId, @RequestBody UpdateRequestDto updateRequestDto) {

    storeService.updateStore(userDetails, storeId, updateRequestDto);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @DeleteMapping("/{storeId}")
  public ResponseEntity<Void> deleteStore(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable UUID storeId) {

    storeService.deleteStore(userDetails, storeId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}