package com.chone.server.domains.ai.controller;

import com.chone.server.domains.ai.dto.request.AiRequestDto;
import com.chone.server.domains.ai.dto.response.GeminiResponseDto;
import com.chone.server.domains.ai.service.AiService;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai")
@RequiredArgsConstructor
public class AiController {

  private final AiService aiService;

  @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
  @PostMapping("/generate-description")
  public ResponseEntity<GeminiResponseDto> generateDescription(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestBody AiRequestDto aiRequestDto) {

    GeminiResponseDto response = aiService.generateDescription(principal.getUser(), aiRequestDto);

    return ResponseEntity.ok(response);
  }
}
