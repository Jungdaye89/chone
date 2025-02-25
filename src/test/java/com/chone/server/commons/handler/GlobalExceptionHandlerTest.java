package com.chone.server.commons.handler;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.exception.ExceptionCode;
import com.chone.server.commons.exception.GlobalExceptionCode;
import com.chone.server.commons.exception.mock.MockApiExceptionCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@WebMvcTest(controllers = {TestController.class})
@AutoConfigureMockMvc(addFilters = false)
class GlobalExceptionHandlerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Test
  void handleApiBusinessException_ShouldReturnExpectedResponse() throws Exception {
    // given
    MockApiExceptionCode exceptionCode = MockApiExceptionCode.NOT_FOUND;
    // when & then
    mockMvc
        .perform(get("/api/test"))
        .andDo(print())
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.httpStatus").value(exceptionCode.getStatus().value()))
        .andExpect(jsonPath("$.errorCode").value(exceptionCode.getCode()))
        .andExpect(jsonPath("$.message").value(exceptionCode.getMessage()))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void handleMethodArgumentNotValid_ShouldReturnValidationErrors() throws Exception {
    // given
    TestRequest request = new TestRequest("");
    ExceptionCode exceptionCode = GlobalExceptionCode.INVALID_INPUT;
    String expectedErrorMessage = "이름은 필수입니다";

    // when & then
    mockMvc
        .perform(
            post("/api/test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.httpStatus").value(exceptionCode.getStatus().value()))
        .andExpect(jsonPath("$.errorCode").value(exceptionCode.getCode()))
        .andExpect(jsonPath("$.message").value(exceptionCode.getMessage()))
        .andExpect(jsonPath("$.details.name").value(expectedErrorMessage))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void handleHttpMessageNotReadable_ShouldReturnBadRequest() throws Exception {
    // given
    String invalidJson = "{invalid-json}";
    ExceptionCode exceptionCode = GlobalExceptionCode.NOT_READABLE;

    // when & then
    mockMvc
        .perform(post("/api/test").contentType(MediaType.APPLICATION_JSON).content(invalidJson))
        .andDo(print())
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCode").value(exceptionCode.getCode()))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Getter
  @AllArgsConstructor
  static class TestRequest {
    @NotBlank(message = "이름은 필수입니다")
    private String name;
  }
}

@RestController
@RequestMapping("/api/test")
class TestController {
  @GetMapping
  public void throwApiException() {
    throw new ApiBusinessException(MockApiExceptionCode.NOT_FOUND);
  }

  @PostMapping
  public void validateRequest(@Valid @RequestBody GlobalExceptionHandlerTest.TestRequest request) {}
}
