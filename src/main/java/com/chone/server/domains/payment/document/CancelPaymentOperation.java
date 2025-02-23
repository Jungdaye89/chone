package com.chone.server.domains.payment.document;

import com.chone.server.domains.payment.dto.response.CancelPaymentResponse;
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
    summary = CancelPaymentOperationConstants.SUMMARY,
    description = CancelPaymentOperationConstants.DESCRIPTION)
@SecurityRequirement(name = CancelPaymentOperationConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "200",
          description = "결제 취소 성공",
          content =
              @Content(
                  mediaType = CancelPaymentOperationConstants.MEDIA_TYPE,
                  schema = @Schema(implementation = CancelPaymentResponse.class),
                  examples =
                      @ExampleObject(value = CancelPaymentOperationConstants.SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = "404",
          description = "결제 내역을 찾을 수 없음",
          content =
              @Content(
                  mediaType = CancelPaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "해당 결제 내역을 찾을 수 없습니다.",
                        value = CancelPaymentOperationConstants.NOT_FOUND_PAYMENT_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "401",
          description = "인증이 필요함",
          content =
              @Content(
                  mediaType = CancelPaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "인증이 필요함",
                        value = CancelPaymentOperationConstants.UNAUTHORIZED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "403",
          description = "접근 권한이 없음",
          content =
              @Content(
                  mediaType = CancelPaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "접근 권한이 없습니다. 해당 주문의 소유자가 아닙니다.",
                        value =
                            CancelPaymentOperationConstants.ORDER_CUSTOMER_ACCESS_DENIED_RESPONSE),
                    @ExampleObject(
                        name = "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.",
                        value =
                            CancelPaymentOperationConstants
                                .ORDER_STORE_OWNER_ACCESS_DENIED_RESPONSE),
                    @ExampleObject(
                        name = "결제 취소 권한이 없습니다.",
                        value = CancelPaymentOperationConstants.CANCEL_PERMISSION_DENIED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "409",
          description = "이미 취소된 결제입니다.",
          content =
              @Content(
                  mediaType = CancelPaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "이미 취소된 결제입니다.",
                        value = CancelPaymentOperationConstants.PAYMENT_ALREADY_CANCELED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청",
          content =
              @Content(
                  mediaType = CancelPaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "실패된 결제입니다(결제된 적이 없습니다).",
                        value = CancelPaymentOperationConstants.FAILED_PAYMENT_RESPONSE),
                    @ExampleObject(
                        name = "주문이 취소될 수 없어 결제를 취소할 수 없습니다.",
                        value = CancelPaymentOperationConstants.ORDER_NOT_CANCELABLE_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "503",
          description = "서비스가 사용 불가능",
          content =
              @Content(
                  mediaType = CancelPaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "결제 취소가 진행 중입니다. 잠시 후 다시 시도해주세요.",
                        value =
                            CancelPaymentOperationConstants
                                .PAYMENT_CANCELLATION_IN_PROGRESS_RESPONSE),
                    @ExampleObject(
                        name = "결제 취소 요청이 거부되었습니다.",
                        value =
                            CancelPaymentOperationConstants.PAYMENT_CANCELLATION_FAILED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "500",
          description = "서버 오류",
          content =
              @Content(
                  mediaType = CancelPaymentOperationConstants.MEDIA_TYPE,
                  examples =
                      @ExampleObject(
                          value =
                              CancelPaymentOperationConstants.PAYMENT_CANCELLATION_ERROR_RESPONSE)))
    })
public @interface CancelPaymentOperation {}

final class CancelPaymentOperationConstants {
  static final String SUMMARY = "결제 취소 API";
  static final String DESCRIPTION =
      """
- 결제를 취소할 수 있는 API입니다.
    - 메서드: **PATCH**
    - 경로: /api/v1/payments/{id}
    - 권한: `CUSTOMER`, `OWNER`, `MANAGER`, `MASTER` 역할 접근 가능
    - 요청: `CancelPaymentRequest` (검증 어노테이션 적용)
    - 응답: 200 OK + `CancelPaymentResponse`

- 역할: 취소 가능 범위
    - `CUSTOMER`: 자신의 결제만 취소 가능
    - `OWNER`: 자신의 매장 결제만 취소 가능
    - `MANAGER`, `MASTER`: 모든 결제 취소 가능
""";
  static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  static final String MEDIA_TYPE = "application/json";

  // == 200 OK 응답 ==
  static final String SUCCESS_EXAMPLE =
      """
{
  "isSuccess": true,
  "status": "결제 취소됨",
  "cancelRequestedAt": "2024-02-21T15:30:00Z"
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
  static final String NOT_FOUND_PAYMENT_RESPONSE =
      """
            {
                "httpMethod": "PATCH",
                "httpStatus": "404",
                "errorCode": "NOT_FOUND_PAYMENT",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "해당 결제 내역을 찾을 수 없습니다.",
                "path": "/api/v1/payments/{id}"
            }
            """;

  static final String ORDER_CUSTOMER_ACCESS_DENIED_RESPONSE =
      """
            {
                "httpMethod": "PATCH",
                "httpStatus": "403",
                "errorCode": "ORDER_CUSTOMER_ACCESS_DENIED",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "접근 권한이 없습니다. 해당 주문의 소유자가 아닙니다.",
                "path": "/api/v1/payments/{id}"
            }
            """;

  static final String ORDER_STORE_OWNER_ACCESS_DENIED_RESPONSE =
      """
            {
                "httpMethod": "PATCH",
                "httpStatus": "403",
                "errorCode": "ORDER_STORE_OWNER_ACCESS_DENIED",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.",
                "path": "/api/v1/payments/{id}"
            }
            """;

  static final String CANCEL_PERMISSION_DENIED_RESPONSE =
      """
            {
                "httpMethod": "PATCH",
                "httpStatus": "403",
                "errorCode": "CANCEL_PERMISSION_DENIED",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "결제 취소 권한이 없습니다.",
                "path": "/api/v1/payments/{id}"
            }
            """;

  static final String PAYMENT_ALREADY_CANCELED_RESPONSE =
      """
            {
                "httpMethod": "PATCH",
                "httpStatus": "409",
                "errorCode": "PAYMENT_ALREADY_CANCELED",
                "timestamp": "2025-02-23
            "httpStatus": "409",
            "errorCode": "PAYMENT_ALREADY_CANCELED",
            "timestamp": "2025-02-23T15:32:45.123456",
            "message": "이미 취소된 결제입니다.",
            "path": "/api/v1/payments/{id}"
        }
        """;

  static final String FAILED_PAYMENT_RESPONSE =
      """
            {
                "httpMethod": "PATCH",
                "httpStatus": "400",
                "errorCode": "FAILED_PAYMENT",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "실패된 결제입니다(결제된 적이 없습니다).",
                "path": "/api/v1/payments/{id}"
            }
            """;

  static final String ORDER_NOT_CANCELABLE_RESPONSE =
      """
            {
                "httpMethod": "PATCH",
                "httpStatus": "400",
                "errorCode": "ORDER_NOT_CANCELABLE",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "주문이 취소될 수 없어 결제를 취소할 수 없습니다.",
                "path": "/api/v1/payments/{id}"
            }
            """;

  static final String PAYMENT_CANCELLATION_IN_PROGRESS_RESPONSE =
      """
            {
                "httpMethod": "PATCH",
                "httpStatus": "503",
                "errorCode": "PAYMENT_CANCELLATION_IN_PROGRESS",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "결제 취소가 진행 중입니다. 잠시 후 다시 시도해주세요.",
                "path": "/api/v1/payments/{id}"
            }
            """;

  static final String PAYMENT_CANCELLATION_ERROR_RESPONSE =
      """
            {
                "httpMethod": "PATCH",
                "httpStatus": "500",
                "errorCode": "PAYMENT_CANCELLATION_ERROR",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "결제 취소 처리 중 오류가 발생했습니다.",
                "path": "/api/v1/payments/{id}"
            }
            """;

  static final String PAYMENT_CANCELLATION_FAILED_RESPONSE =
      """
            {
                "httpMethod": "PATCH",
                "httpStatus": "503",
                "errorCode": "PAYMENT_CANCELLATION_FAILED",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "결제 취소 요청이 거부되었습니다.",
                "path": "/api/v1/payments/{id}"
            }
            """;
}
