package com.chone.server.domains.review.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.review.dto.request.CreateRequestDto;
import com.chone.server.domains.review.dto.request.ReviewListRequestDto;
import com.chone.server.domains.review.dto.response.ReviewListResponseDto;
import com.chone.server.domains.review.dto.response.ReviewPageResponseDto;
import com.chone.server.domains.review.dto.response.ReviewResponseDto;
import com.chone.server.domains.review.exception.ReviewExceptionCode;
import com.chone.server.domains.review.service.ReviewService;
import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.multipart.MultipartFile;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

  private static final Logger log = LoggerFactory.getLogger(ReviewControllerTest.class);

  @Autowired private MockMvc mockMvc;

  @MockitoBean private ReviewService reviewService;

  private CreateRequestDto requestDto;

  private CustomUserDetails customUserDetails;

  @BeforeEach
  void setUp() {
    log.info("=== SETUP START ===");
    customUserDetails = createCustomUserDetails("CUSTOMER");
    log.info("CustomUserDetails 생성: {}", customUserDetails);

    requestDto =
        CreateRequestDto.builder()
            .orderId(UUID.fromString("dfff2bdb-0e4e-4e57-a81c-038beb7d1544"))
            .storeId(UUID.fromString("564c0157-d342-47fc-aefd-13229b000276"))
            .content("리뷰 내용")
            .rating(new BigDecimal("4.5"))
            .isPublic(true)
            .build();
  }

  private CustomUserDetails createCustomUserDetails(String role) {
    User user =
        User.builder("testUserId", "test@example.com", "hashedPassword", Role.valueOf(role), true)
            .build();
    log.info("테스트용 User 생성: {}", user);
    return new CustomUserDetails(user);
  }

  private MockMultipartFile createMockMultipartFile(String jsonString) {
    return new MockMultipartFile("data", "", "application/json", jsonString.getBytes());
  }

  private ResultActions performMultipartRequest(
      String role, CreateRequestDto requestDto, MockMultipartFile file) throws Exception {

    MockMultipartFile data =
        createMockMultipartFile(new ObjectMapper().writeValueAsString(requestDto));

    CustomUserDetails customUserDetails = createCustomUserDetails(role);

    return mockMvc.perform(
        multipart("/api/v1/reviews")
            .file(data)
            .file(file)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON)
            .with(
                authentication(
                    new UsernamePasswordAuthenticationToken(
                        customUserDetails, null, customUserDetails.getAuthorities()))));
  }

  private ResultActions performGetRequest(String page, String size) throws Exception {
    log.info("=== GET 요청: page={}, size={} ===", page, size);
    ResultActions resultActions =
        mockMvc.perform(
            get("/api/v1/reviews")
                .param("page", page)
                .param("size", size)
                .with(
                    authentication(
                        new UsernamePasswordAuthenticationToken(
                            customUserDetails, null, customUserDetails.getAuthorities()))));

    log.info("=== 요청 결과: {} ===", resultActions.andReturn().getResponse().getContentAsString());
    return resultActions;
  }

  @Nested
  @DisplayName("createReview 성공 케이스")
  class createReview_Success {

    private ReviewResponseDto createReviewResponse() {
      return ReviewResponseDto.builder()
          .reviewId(UUID.randomUUID())
          .createdAt(LocalDateTime.now())
          .build();
    }

    private MockMultipartFile createMockFile(String fileName, String contentType, byte[] content) {
      return new MockMultipartFile("file", fileName, contentType, content);
    }

    private void executeCreateReviewTest(
        CreateRequestDto requestDto, MockMultipartFile file, String testCaseDescription)
        throws Exception {
      ReviewResponseDto responseDto = createReviewResponse();
      when(reviewService.createReview(any(CreateRequestDto.class), any(), any()))
          .thenReturn(responseDto);

      ResultActions resultActions = performMultipartRequest("CUSTOMER", requestDto, file);

      String resultJson =
          resultActions
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.reviewId").exists())
              .andExpect(jsonPath("$.createdAt").exists())
              .andReturn()
              .getResponse()
              .getContentAsString();

      System.out.println("[SUCCESS] " + testCaseDescription + " 결과: " + resultJson);
    }

    @Test
    @DisplayName("리뷰 생성 성공 - 모든 필드가 올바른 경우 (이미지 포함)")
    void createReview_Success_WithAllFields() throws Exception {

      MockMultipartFile file =
          createMockFile("test-image.jpg", "image/jpeg", "dummy image content".getBytes());
      executeCreateReviewTest(requestDto, file, "모든 필드가 올바른 경우 (이미지 포함)");
    }

    @Test
    @DisplayName("리뷰 생성 성공 - 이미지 없이 텍스트만 있는 경우")
    void createReview_Success_WithoutImage() throws Exception {

      MockMultipartFile file = createMockFile("", "application/octet-stream", new byte[0]);
      executeCreateReviewTest(requestDto, file, "이미지 없이 텍스트만 있는 경우");
    }

    @Test
    @DisplayName("리뷰 생성 성공 - isPublic이 false인 경우 (비공개 리뷰)")
    void createReview_Success_IsPublicFalse() throws Exception {

      CreateRequestDto privateRequestDto =
          CreateRequestDto.builder()
              .orderId(requestDto.getOrderId())
              .storeId(requestDto.getStoreId())
              .content(requestDto.getContent())
              .rating(requestDto.getRating())
              .isPublic(false)
              .build();

      MockMultipartFile file = createMockFile("", "application/octet-stream", new byte[0]);
      executeCreateReviewTest(privateRequestDto, file, "isPublic이 false인 경우 (비공개 리뷰)");
    }
  }

  @Nested
  @DisplayName("createReview 실패 케이스")
  class createReview_FailureCases {

    private MockMultipartFile createMockFile(String fileName, String contentType, byte[] content) {
      return new MockMultipartFile("file", fileName, contentType, content);
    }

    private void mockReviewServiceException(ReviewExceptionCode exceptionCode) {
      when(reviewService.createReview(
              any(CreateRequestDto.class), any(MultipartFile.class), any(User.class)))
          .thenThrow(new ApiBusinessException(exceptionCode));
    }

    private void executeFailureTest(
        ReviewExceptionCode exceptionCode,
        String errorCode,
        String errorMessage,
        MockMultipartFile file,
        String testCaseDescription,
        int expectedStatus)
        throws Exception {

      mockReviewServiceException(exceptionCode);

      ResultActions resultActions = performMultipartRequest("CUSTOMER", requestDto, file);

      String resultJson =
          resultActions
              .andExpect(status().is(expectedStatus))
              .andExpect(jsonPath("$.errorCode").value(errorCode))
              .andExpect(jsonPath("$.message").value(errorMessage))
              .andReturn()
              .getResponse()
              .getContentAsString();

      System.out.println("[FAILURE] " + testCaseDescription + " 결과: " + resultJson);
    }

    @Test
    @DisplayName("주문하지 않은 사용자 (REVIEW_FORBIDDEN)")
    void createReview_Fail_Forbidden() throws Exception {

      MockMultipartFile file =
          createMockFile("test-image.jpg", "image/jpeg", "dummy image content".getBytes());
      executeFailureTest(
          ReviewExceptionCode.REVIEW_FORBIDDEN,
          "REVIEW_FORBIDDEN",
          "주문한 고객만 리뷰를 작성할 수 있습니다.",
          file,
          "주문하지 않은 사용자",
          403);
    }

    @Test
    @DisplayName("존재하지 않는 가게 ID (STORE_NOT_FOUND)")
    void createReview_Fail_StoreNotFound() throws Exception {

      MockMultipartFile file =
          createMockFile("test-image.jpg", "image/jpeg", "dummy image content".getBytes());
      executeFailureTest(
          ReviewExceptionCode.STORE_NOT_FOUND,
          "STORE_NOT_FOUND",
          "해당 가게를 찾을 수 없습니다.",
          file,
          "존재하지 않는 가게 ID",
          404);
    }

    @Test
    @DisplayName("존재하지 않는 주문 ID (ORDER_NOT_FOUND)")
    void createReview_Fail_OrderNotFound() throws Exception {

      MockMultipartFile file =
          createMockFile("test-image.jpg", "image/jpeg", "dummy image content".getBytes());
      executeFailureTest(
          ReviewExceptionCode.ORDER_NOT_FOUND,
          "ORDER_NOT_FOUND",
          "해당 주문을 찾을 수 없습니다.",
          file,
          "존재하지 않는 주문 ID",
          404);
    }

    @Test
    @DisplayName("이미 리뷰를 작성한 주문 (REVIEW_ALREADY_EXISTS)")
    void createReview_Fail_ReviewAlreadyExists() throws Exception {

      MockMultipartFile file =
          createMockFile("test-image.jpg", "image/jpeg", "dummy image content".getBytes());
      executeFailureTest(
          ReviewExceptionCode.REVIEW_ALREADY_EXISTS,
          "REVIEW_ALREADY_EXISTS",
          "해당 주문에 대한 리뷰가 이미 존재합니다.",
          file,
          "이미 리뷰를 작성한 주문",
          409);
    }

    @Test
    @DisplayName("이미지 업로드 중 오류 (FILE_UPLOAD_ERROR)")
    void createReview_Fail_FileUploadError() throws Exception {

      MockMultipartFile file =
          createMockFile("test.png", "image/png", "test image content".getBytes());
      executeFailureTest(
          ReviewExceptionCode.FILE_UPLOAD_ERROR,
          "FILE_UPLOAD_ERROR",
          "이미지 업로드 중 오류가 발생했습니다.",
          file,
          "이미지 업로드 중 오류",
          500);
    }
  }

  @Nested
  @DisplayName("리뷰 목록 조회 - 페이지네이션")
  class getReviews_Pagination {

    private ReviewListRequestDto filterParams;
    private Pageable pageable;

    @BeforeEach
    void setUpForPagination() {
      filterParams = new ReviewListRequestDto();
    }

    @Test
    @DisplayName("page 파라미터가 유효한 값일 때 해당 페이지 조회")
    void getReviews_WhenPageIsValid_ShouldReturnRequestedPage() throws Exception {

      log.info("=== [TEST START] page 파라미터가 유효한 값일 때 해당 페이지 조회 ===");
      pageable = PageRequest.of(1, 10);

      List<ReviewPageResponseDto> content = Collections.emptyList();
      Page<ReviewPageResponseDto> mockPage = new PageImpl<>(content, pageable, 0);
      ReviewListResponseDto mockResponseDto = ReviewListResponseDto.from(mockPage);

      log.info("Mocking 데이터 설정: {}", mockResponseDto);
      when(reviewService.getReviews(any(ReviewListRequestDto.class), any(), any()))
          .thenReturn(mockResponseDto);

      ResultActions resultActions = performGetRequest("1", "10");

      log.info("요청 파라미터: page=1, size=10");
      log.info("예상 결과: status=200, page=1");
      log.info("실제 응답 상태 코드: {}", resultActions.andReturn().getResponse().getStatus());
      log.info("응답 결과: {}", resultActions.andReturn().getResponse().getContentAsString());

      resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.pageInfo.page").value(1));
    }

    @Test
    @DisplayName("page 파라미터가 비어있을 때 기본값 0 적용")
    void getReviews_WhenPageIsEmpty_ShouldApplyDefaultPage() throws Exception {

      log.info("=== [TEST START] page 파라미터가 비어있을 때 기본값 0 적용 ===");
      pageable = PageRequest.of(0, 10);

      List<ReviewPageResponseDto> content = Collections.emptyList();
      Page<ReviewPageResponseDto> mockPage = new PageImpl<>(content, pageable, 0);
      ReviewListResponseDto mockResponseDto = ReviewListResponseDto.from(mockPage);

      log.info("Mocking 데이터 설정: {}", mockResponseDto);
      when(reviewService.getReviews(any(ReviewListRequestDto.class), any(), any()))
          .thenReturn(mockResponseDto);

      ResultActions resultActions = performGetRequest("", "10");

      log.info("응답 결과: {}", resultActions.andReturn().getResponse().getContentAsString());
      resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.pageInfo.page").value(0));
    }

    @Test
    @DisplayName("page 파라미터가 없을 때 기본값 0 적용")
    void getReviews_WhenPageIsNull_ShouldApplyDefaultPage() throws Exception {

      log.info("=== [TEST START] page 파라미터가 없을 때 기본값 0 적용 ===");
      pageable = PageRequest.of(0, 10);

      List<ReviewPageResponseDto> content = Collections.emptyList();
      Page<ReviewPageResponseDto> mockPage = new PageImpl<>(content, pageable, 0);
      ReviewListResponseDto mockResponseDto = ReviewListResponseDto.from(mockPage);

      log.info("Mocking 데이터 설정: {}", mockResponseDto);
      when(reviewService.getReviews(any(ReviewListRequestDto.class), any(), any()))
          .thenReturn(mockResponseDto);

      ResultActions resultActions =
          mockMvc.perform(
              get("/api/v1/reviews")
                  .param("size", "10")
                  .with(
                      authentication(
                          new UsernamePasswordAuthenticationToken(
                              customUserDetails, null, customUserDetails.getAuthorities()))));

      log.info("응답 결과: {}", resultActions.andReturn().getResponse().getContentAsString());
      resultActions.andExpect(status().isOk()).andExpect(jsonPath("$.pageInfo.page").value(0));
    }
  }
}
