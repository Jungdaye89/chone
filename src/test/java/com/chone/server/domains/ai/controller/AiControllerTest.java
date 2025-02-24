package com.chone.server.domains.ai.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chone.server.ChoneApplication;
import com.chone.server.domains.ai.dto.request.AiRequestDto;
import com.chone.server.domains.ai.dto.response.GeminiResponseDto;
import com.chone.server.domains.ai.service.AiService;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.auth.service.CustomUserDetailsService;
import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@ExtendWith(MockitoExtension.class)
@SpringBootTest(classes = ChoneApplication.class)
@AutoConfigureMockMvc
public class AiControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private AiService aiService;

  @MockitoBean private CustomUserDetailsService customUserDetailsService;

  private CustomUserDetails createCustomUserDetails(String role) {
    User user =
        User.builder("testMember", "test@example.com", "hashedPassword", Role.valueOf(role), true)
            .build();

    return new CustomUserDetails(user);
  }

  private ResultActions performGenerateDescription(String role) throws Exception {

    return mockMvc.perform(
        post("/api/v1/ai/generate-description")
            .contentType(MediaType.APPLICATION_JSON)
            .content(
                """
                        {
                            "storeId": "e4d1f5d2-3c89-4b99-aeef-2b4665d707a3",
                            "productId": "acb12345-6789-0abc-def1-234567890abc",
                            "text": "테스트 설명"
                        }
                        """)
            .with(user(createCustomUserDetails(role))));
  }

  @Nested
  @DisplayName("성공 케이스")
  class SuccessCases {

    @Test
    @DisplayName("ROLE_MANAGER - 상품 설명 생성 성공")
    void testGenerateDescription_Success_Manager() throws Exception {

      GeminiResponseDto responseDto = GeminiResponseDto.builder().text("테스트 설명 생성 성공").build();

      when(aiService.generateDescription(any(User.class), any(AiRequestDto.class)))
          .thenReturn(responseDto);

      performGenerateDescription("MANAGER")
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.text").value("테스트 설명 생성 성공"));

      String result =
          performGenerateDescription("MASTER").andReturn().getResponse().getContentAsString();
      System.out.println("MANAGER 테스트 결과: " + result);
    }

    @Test
    @DisplayName("ROLE_OWNER - 상품 설명 생성 성공")
    void testGenerateDescription_Success_Owner() throws Exception {

      GeminiResponseDto responseDto = GeminiResponseDto.builder().text("테스트 설명 생성 성공").build();

      when(aiService.generateDescription(any(User.class), any(AiRequestDto.class)))
          .thenReturn(responseDto);

      performGenerateDescription("OWNER")
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.text").value("테스트 설명 생성 성공"));

      String result =
          performGenerateDescription("MASTER").andReturn().getResponse().getContentAsString();
      System.out.println("OWNER 테스트 결과: " + result);
    }

    @Test
    @DisplayName("ROLE_MASTER - 상품 설명 생성 성공")
    void testGenerateDescription_Success_Master() throws Exception {

      GeminiResponseDto responseDto = GeminiResponseDto.builder().text("테스트 설명 생성 성공").build();

      when(aiService.generateDescription(any(User.class), any(AiRequestDto.class)))
          .thenReturn(responseDto);

      performGenerateDescription("MASTER")
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.text").value("테스트 설명 생성 성공"));

      String result =
          performGenerateDescription("MASTER").andReturn().getResponse().getContentAsString();
      System.out.println("MASTER 테스트 결과: " + result);
    }
  }

  @Nested
  @DisplayName("권한 없음 (403 Forbidden)")
  class ForbiddenCases {

    @Test
    @DisplayName("ROLE_CUSTOMER - 상품 설명 생성 실패 (403 Forbidden)")
    void testGenerateDescription_Forbidden_Customer() throws Exception {

      String result =
          performGenerateDescription("CUSTOMER")
              .andExpect(status().isForbidden())
              .andReturn()
              .getResponse()
              .getContentAsString();

      System.out.println("CUSTOMER 테스트 결과 (403 Forbidden): " + result);
    }
  }

  @Nested
  @DisplayName("유효하지 않은 요청 데이터 (400 Bad Request)")
  class InvalidRequestCases {

    @Test
    @WithMockUser(
        username = "testUser",
        roles = {"OWNER"})
    public void testGenerateDescription_Invalid_NullText() throws Exception {

      AiRequestDto requestDto = new AiRequestDto();
      requestDto.setStoreId(UUID.fromString("e4d1f5d2-3c89-4b99-aeef-2b4665d707a3"));
      requestDto.setProductId(UUID.fromString("acb12345-6789-0abc-def1-234567890abc"));
      requestDto.setText(null);

      String requestBody = new ObjectMapper().writeValueAsString(requestDto);

      mockMvc
          .perform(
              post("/api/v1/ai/generate-description")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(status().isBadRequest())
          .andDo(
              result -> {
                System.out.println(
                    "Invalid Null Text 테스트 결과: " + result.getResponse().getContentAsString());
              });
    }

    @Test
    @WithMockUser(
        username = "testUser",
        roles = {"OWNER"})
    public void testGenerateDescription_Invalid_EmptyText() throws Exception {

      AiRequestDto requestDto = new AiRequestDto();
      requestDto.setStoreId(UUID.fromString("e4d1f5d2-3c89-4b99-aeef-2b4665d707a3"));
      requestDto.setProductId(UUID.fromString("acb12345-6789-0abc-def1-234567890abc"));
      requestDto.setText("");

      String requestBody = new ObjectMapper().writeValueAsString(requestDto);

      mockMvc
          .perform(
              post("/api/v1/ai/generate-description")
                  .contentType(MediaType.APPLICATION_JSON)
                  .content(requestBody))
          .andExpect(status().isBadRequest())
          .andDo(
              result -> {
                System.out.println(
                    "Invalid Empty Text 테스트 결과: " + result.getResponse().getContentAsString());
              });
    }
  }
}
