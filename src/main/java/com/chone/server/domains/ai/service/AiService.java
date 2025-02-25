package com.chone.server.domains.ai.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.exception.GlobalExceptionCode;
import com.chone.server.commons.facade.AiFacade;
import com.chone.server.commons.facade.ProductFacade;
import com.chone.server.domains.ai.dto.request.AiRequestDto;
import com.chone.server.domains.ai.dto.request.GeminiApiRequestDto;
import com.chone.server.domains.ai.dto.response.GeminiApiResponseDto;
import com.chone.server.domains.ai.dto.response.GeminiResponseDto;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.user.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AiService {

  private final RestClient restClient;
  private final ProductFacade productFacade;
  private final AiFacade aiFacade;

  @Value("${gemini.api.key}")
  private String geminiApiKey;

  public AiService(
      RestClient.Builder restClientBuilder, ProductFacade productFacade, AiFacade aiFacade) {
    this.restClient = restClientBuilder.build();
    this.productFacade = productFacade;
    this.aiFacade = aiFacade;
  }

  @Transactional
  public GeminiResponseDto generateDescription(User user, AiRequestDto aiRequestDto) {

    Product product = productFacade.findByIdAndStoreUser(aiRequestDto.getProductId(), user);

    String text = limitTextLength(aiRequestDto.getText()) + " 답변을 최대한 간결하게 50자 이하로";

    GeminiApiRequestDto geminiApiRequestDto = GeminiApiRequestDto.from(text);

    try {
      String apiKey = geminiApiKey;

      GeminiApiResponseDto geminiApiResponseDto =
          restClient
              .post()
              .uri(
                  "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent?key="
                      + apiKey)
              .header("Content-Type", "application/json")
              .body(geminiApiRequestDto)
              .retrieve()
              .body(GeminiApiResponseDto.class);

      String extractedText = extractTextFromResponse(geminiApiResponseDto);

      productFacade.updateDescription(product, extractedText);

      aiFacade.saveAiRecord(product, user, text, extractedText);

      return GeminiResponseDto.builder().text(extractedText).build();

    } catch (Exception e) {
      throw new ApiBusinessException(GlobalExceptionCode.INTERNAL_ERROR);
    }
  }

  private String limitTextLength(String text) {
    if (text == null) {
      return "";
    }
    return text.length() > 255 ? text.substring(0, 255) : text;
  }

  private String extractTextFromResponse(GeminiApiResponseDto responseDto) {
    return responseDto.getCandidates().stream()
        .findFirst()
        .map(GeminiApiResponseDto.Candidate::getContent)
        .map(GeminiApiResponseDto.Content::getParts)
        .flatMap(parts -> parts.stream().findFirst())
        .map(GeminiApiResponseDto.Part::getText)
        .orElse("AI 응답이 없습니다.");
  }
}
