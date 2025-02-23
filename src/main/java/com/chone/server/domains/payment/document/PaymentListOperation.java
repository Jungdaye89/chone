package com.chone.server.domains.payment.document;

import com.chone.server.domains.payment.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(
    summary = PaymentListOperationConstants.SUMMARY,
    description = PaymentListOperationConstants.DESCRIPTION)
@SecurityRequirement(name = PaymentListOperationConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "200",
          description = "결제 목록 조회 성공",
          content =
              @Content(
                  mediaType = PaymentListOperationConstants.MEDIA_TYPE,
                  schema = @Schema(implementation = PageResponse.class),
                  examples =
                      @ExampleObject(value = PaymentListOperationConstants.SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = "401",
          description = "인증이 필요함",
          content =
              @Content(
                  mediaType = PaymentListOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "인증이 필요함",
                        value = PaymentListOperationConstants.UNAUTHORIZED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "403",
          description = "접근 권한이 없음",
          content =
              @Content(
                  mediaType = PaymentListOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "사용자별 주문 조회 권한이 없음",
                        value =
                            PaymentListOperationConstants
                                .CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED_RESPONSE),
                    @ExampleObject(
                        name = "가게별 주문 조회 권한이 없음",
                        value =
                            PaymentListOperationConstants
                                .STORE_PAYMENT_FILTERING_ACCESS_DENIED_RESPONSE)
                  }))
    })
public @interface PaymentListOperation {}

final class PaymentListOperationConstants {
  static final String SUMMARY = "결제 목록 조회 API";
  static final String DESCRIPTION =
      """
- 결제 내역을 조회할 수 있는 API입니다.
    - 메서드: GET
    - 경로: /api/v1/payments
    - 권한: 모든 역할 접근 가능 (역할별 조회 범위 제한)
    - 페이지네이션: page, size, sort 지원 (기본값: page=0, size=10, sort=createdat,desc)
    - 응답: 200 OK

- 역할별 조회 가능 범위
    - CUSTOMER
        - 조회 가능 범위: 자신의 결제만 조회
        - 필터링 제한: 고객 필터링 불가, 매장 필터링 가능
    - OWNER
        - 조회 가능 범위: 자신의 매장 결제만 조회
        - 필터링 제한: 고객 필터링 가능, 매장 필터링 불가
    - MANAGER, MASTER
        - 조회 가능 범위: 모든 결제 조회
        - 필터링 제한: 모든 필터링 가능
""";
  static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  static final String MEDIA_TYPE = "application/json";

  // == 200 OK 응답 ==
  static final String SUCCESS_EXAMPLE =
      """
        {
            "content": [
                {
                    "id": "123e4567-e89b-12d3-a456-426614174000",
                    "orderId": "123e4567-e89b-12d3-a456-426614174001",
                    "totalPrice": 15000,
                    "method": "신용카드",
                    "status": "결제완료",
                    "createdAt": "2024-02-12T15:30:00Z",
                    "updatedAt": "2024-02-12T15:30:00Z"
                }
            ],
            "pageInfo": {
                "page": 0,
                "size": 10,
                "totalElements": 50,
                "totalPages": 5
            }
        }
    """;

  // == 에러 응답 예시 ==
  static final String UNAUTHORIZED_RESPONSE =
      """
                                      {
                                        "httpMethod": "PATCH",
                                        "httpStatus": "401",
                                        "errorCode": "UNAUTHORIZED",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                                        "path": "/api/v1/orders/{id}"
                                      }
                                  """;
  static final String CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED_RESPONSE =
      """
            {
                "httpMethod": "GET",
                "httpStatus": "403",
                "errorCode": "CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "사용자별 주문 조회 권한이 없습니다.",
                "path": "/api/v1/payments"
            }
            """;

  static final String STORE_PAYMENT_FILTERING_ACCESS_DENIED_RESPONSE =
      """
            {
                "httpMethod": "GET",
                "httpStatus": "403",
                "errorCode": "STORE_PAYMENT_FILTERING_ACCESS_DENIED",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "가게별 주문 조회 권한이 없습니다.",
                "path": "/api/v1/payments"
            }
            """;
}
