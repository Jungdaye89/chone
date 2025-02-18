package com.chone.server.domains.ai.service;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.exception.GlobalExceptionCode;
import com.chone.server.domains.ai.domain.Ai;
import com.chone.server.domains.ai.dto.request.AiRequestDto;
import com.chone.server.domains.ai.dto.request.GeminiApiRequestDto;
import com.chone.server.domains.ai.dto.response.GeminiApiResponseDto;
import com.chone.server.domains.ai.dto.response.GeminiResponseDto;
import com.chone.server.domains.ai.exception.AiExceptionCode;
import com.chone.server.domains.ai.repository.AiRepository;
import com.chone.server.domains.product.domain.Product;
import com.chone.server.domains.product.repository.ProductRepository;
import com.chone.server.domains.user.domain.User;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Service
public class AiService {

  private final RestClient restClient;
  private final AiRepository aiRepository;
  private final ProductRepository productRepository;

  @Value("${gemini.api.key}")
  private String geminiApiKey;

  public AiService(
      RestClient.Builder restClientBuilder,
      AiRepository aiRepository,
      ProductRepository productRepository) {
    this.restClient = restClientBuilder.build();
    this.aiRepository = aiRepository;
    this.productRepository = productRepository;
  }

  @Transactional
  public GeminiResponseDto generateDescription(User user, AiRequestDto aiRequestDto) {

    Product product =
        productRepository
            .findByIdAndStoreUser(aiRequestDto.getProductId(), user)
            .orElseThrow(() -> new ApiBusinessException(AiExceptionCode.PRODUCT_NOT_FOUND));

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

      product.updateDescription(extractedText);

      Ai ai = Ai.builder(product.getStore(), product, text, extractedText).build();

      ai.updateCreatedBy(user.getUsername());

      aiRepository.save(ai);

      return GeminiResponseDto.builder().text(extractedText).build();

    } catch (HttpClientErrorException e) {
      handleHttpClientError(e);
      throw new ApiBusinessException(GlobalExceptionCode.INTERNAL_ERROR);
    } catch (RestClientResponseException e) {
      handleRestClientError(e);
      throw new ApiBusinessException(GlobalExceptionCode.INTERNAL_ERROR);
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

  private void handleHttpClientError(HttpClientErrorException e) {
    switch (HttpStatus.valueOf(e.getStatusCode().value())) {
      case BAD_REQUEST -> throw new ApiBusinessException(GlobalExceptionCode.INVALID_REQUEST);
      case UNAUTHORIZED -> throw new ApiBusinessException(AiExceptionCode.AI_UNAUTHORIZED);
      case FORBIDDEN -> throw new ApiBusinessException(AiExceptionCode.AI_FORBIDDEN);
      default -> throw new ApiBusinessException(GlobalExceptionCode.INTERNAL_ERROR);
    }
  }

  private void handleRestClientError(RestClientResponseException e) {
    switch (HttpStatus.valueOf(e.getStatusCode().value())) {
      case INTERNAL_SERVER_ERROR ->
          throw new ApiBusinessException(AiExceptionCode.AI_SERVICE_ERROR);
      case SERVICE_UNAVAILABLE ->
          throw new ApiBusinessException(AiExceptionCode.AI_SERVICE_UNAVAILABLE);
      default -> throw new ApiBusinessException(GlobalExceptionCode.INTERNAL_ERROR);
    }
  }
}
