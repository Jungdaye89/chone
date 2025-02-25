package com.chone.server.domains.user.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.dto.request.SignupRequestDto;
import com.chone.server.domains.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

  private final ObjectMapper objectMapper = new ObjectMapper();
  @Autowired
  private MockMvc mockMvc;
  @MockitoBean
  private UserService userService;

  @Test
  @WithMockUser // 인증된 사용자로 요청을 보냄
  @DisplayName("회원가입 성공 테스트")
  void signup_Success() throws Exception {
    // Given
    SignupRequestDto requestDto = new SignupRequestDto("testuser1", "Password123!",
        "test@example.com", Role.CUSTOMER);

    Mockito.doNothing().when(userService).signUp(Mockito.any(SignupRequestDto.class));

    // When & Then
    mockMvc.perform(post("/api/v1/users/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))) // ✅ content() 메서드는 정상 동작함
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.status").value(201))
        .andExpect(jsonPath("$.message").value("회원가입성공"))
        .andExpect(jsonPath("$.code").value("SUCCESS"));
  }

  @Test
  @DisplayName("회원가입 실패 테스트 - 필수 필드 누락")
  void signup_Failure_MissingFields() throws Exception {
    // Given
    SignupRequestDto requestDto = new SignupRequestDto("", "", "invalidEmail", Role.CUSTOMER);

    // When & Then
    mockMvc.perform(post("/api/v1/users/signup")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto)))
        .andExpect(status().isBadRequest());
  }
}