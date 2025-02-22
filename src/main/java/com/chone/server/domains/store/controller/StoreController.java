package com.chone.server.domains.store.controller;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.store.dto.request.CreateRequestDto;
import com.chone.server.domains.store.dto.request.UpdateRequestDto;
import com.chone.server.domains.store.dto.response.CreateResponseDto;
import com.chone.server.domains.store.dto.response.ReadResponseDto;
import com.chone.server.domains.store.dto.response.SearchResponseDto;
import com.chone.server.domains.store.service.StoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "가게", description = "가게 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/stores")
public class StoreController {

  private final StoreService storeService;

  @Operation(summary = "가게 등록 API", description = "MANAGER, MASTER 계정이 OWNER 계정의 가게를 등록할 수 있습니다.")
  @PostMapping
  @PreAuthorize("hasAnyRole('MASTER', 'MANAGER')")
  public ResponseEntity<CreateResponseDto> createStore(
      @RequestBody CreateRequestDto createRequestDto) {

    CreateResponseDto createResponseDto = storeService.createStore(createRequestDto);

    return ResponseEntity.ok(createResponseDto);
  }

  @Operation(summary = "가게 검색 API", description = "해당 조건으로 가게를 검색합니다.")
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

    SearchResponseDto searchResponseDto = storeService.searchStores(page, size, sort, direction,
        startDate, endDate, category, sido, sigungu, dong, userId);

    return ResponseEntity.ok(searchResponseDto);
  }

  @Operation(summary = "가게 조회 API", description = "가게를 조회합니다.")
  @GetMapping("/{storeId}")
  public ResponseEntity<ReadResponseDto> getStore(@PathVariable("storeId") UUID storeId) {

    ReadResponseDto readResponseDto = storeService.getStore(storeId);

    return ResponseEntity.ok(readResponseDto);
  }

  @Operation(summary = "가게 수정 API", description = "OWNER, MANAGER, OWNER 계정만 가게를 수정합니다.")
  @PutMapping("/{storeId}")
  @PreAuthorize("!hasRole('CUSTOMER')")
  public ResponseEntity<Void> updateStore(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("storeId") UUID storeId, @RequestBody UpdateRequestDto updateRequestDto) {

    storeService.updateStore(userDetails.getUser(), storeId, updateRequestDto);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @Operation(summary = "가게 삭제 API", description = "MANAGER, OWNER 계정만 가게를 삭제합니다.")
  @DeleteMapping("/{storeId}")
  @PreAuthorize("hasAnyRole('MANAGER', 'MASTER')")
  public ResponseEntity<Void> deleteStore(@AuthenticationPrincipal CustomUserDetails userDetails,
      @PathVariable("storeId") UUID storeId) {

    storeService.deleteStore(userDetails.getUser(), storeId);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}