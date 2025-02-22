package com.chone.server.domains.ai.controller;

import com.chone.server.domains.ai.dto.request.AiRequestDto;
import com.chone.server.domains.ai.dto.response.GeminiResponseDto;
import com.chone.server.domains.ai.service.AiService;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

  @Operation(summary = "상품 설명 생성 API", description = "AI를 이용해 상품 설명을 자동으로 생성한다.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "상품 설명 생성 성공",
        content =
            @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = GeminiResponseDto.class),
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 200,
                                    "message": "상품 설명 생성 성공",
                                    "data": {
                                        "text": "AI가 생성한 상품 설명 예시"
                                    }
                                }
                                """))),
    @ApiResponse(
        responseCode = "403",
        description = "상품 설명 생성 실패",
        content =
            @Content(
                mediaType = "application/json",
                examples =
                    @ExampleObject(
                        value =
                            """
                                {
                                    "status": 403,
                                    "message": "상품 설명 생성 실패",
                                    "code": "FAIL"
                                }
                                """)))
  })
  @PreAuthorize("hasAnyRole('OWNER', 'MASTER', 'MANAGER')")
  @PostMapping("/generate-description")
  public ResponseEntity<GeminiResponseDto> generateDescription(
      @AuthenticationPrincipal CustomUserDetails principal,
      @RequestBody AiRequestDto aiRequestDto) {

    GeminiResponseDto response = aiService.generateDescription(principal.getUser(), aiRequestDto);

    return ResponseEntity.ok(response);
  }
}
