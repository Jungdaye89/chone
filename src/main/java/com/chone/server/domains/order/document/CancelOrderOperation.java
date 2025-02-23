package com.chone.server.domains.order.document;

import com.chone.server.domains.order.dto.response.CancelOrderResponse;
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
    summary = CancelOrderOperationConstants.SUMMARY,
    description = CancelOrderOperationConstants.DESCRIPTION)
@SecurityRequirement(name = CancelOrderOperationConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = CancelOrderOperationConstants.SUCCESS_RESPONSE_CODE,
          description = "주문 취소 성공",
          content =
              @Content(
                  mediaType = CancelOrderOperationConstants.MEDIA_TYPE,
                  schema = @Schema(implementation = CancelOrderResponse.class),
                  examples =
                      @ExampleObject(value = CancelOrderOperationConstants.SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = "401",
          description = "인증이 필요함",
          content =
              @Content(
                  mediaType = CancelOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "인증이 필요함",
                        value = CancelOrderOperationConstants.UNAUTHORIZED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "404",
          description = "주문을 찾을 수 없음",
          content =
              @Content(
                  mediaType = CancelOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "해당 주문을 찾을 수 없습니다.",
                        value = CancelOrderOperationConstants.NOT_FOUND_ORDER_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "403",
          description = "접근 권한이 없음",
          content =
              @Content(
                  mediaType = CancelOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "접근 권한이 없습니다. 해당 주문의 소유자가 아닙니다.",
                        value =
                            CancelOrderOperationConstants.ORDER_CUSTOMER_ACCESS_DENIED_RESPONSE),
                    @ExampleObject(
                        name = "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.",
                        value =
                            CancelOrderOperationConstants.ORDER_STORE_OWNER_ACCESS_DENIED_RESPONSE),
                    @ExampleObject(
                        name = "주문 취소 권한이 없습니다.",
                        value =
                            CancelOrderOperationConstants.ORDER_CANCEL_PERMISSION_DENIED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청",
          content =
              @Content(
                  mediaType = CancelOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "이미 취소된 주문입니다.",
                        value = CancelOrderOperationConstants.ORDER_ALREADY_CANCELED_RESPONSE),
                    @ExampleObject(
                        name = "취소할 수 없는 주문입니다.",
                        value = CancelOrderOperationConstants.ORDER_NOT_CANCELABLE_RESPONSE),
                    @ExampleObject(
                        name = "주문 생성 후 5분이 경과하여 취소할 수 없습니다.",
                        value = CancelOrderOperationConstants.ORDER_CANCELLATION_TIMEOUT_RESPONSE),
                    @ExampleObject(
                        name = "실패된 결제입니다(결제된 적이 없습니다).",
                        value = CancelOrderOperationConstants.FAILED_PAYMENT_RESPONSE),
                    @ExampleObject(
                        name = "취소 이유는 필수입니다.",
                        value = CancelOrderOperationConstants.ORDER_REASON_NULL_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "409",
          description = "충돌 발생",
          content =
              @Content(
                  mediaType = CancelOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "완료된 주문은 취소할 수 없습니다.",
                        value = CancelOrderOperationConstants.ORDER_ALREADY_COMPLETED_RESPONSE),
                    @ExampleObject(
                        name = "음식 준비가 시작된 주문은 취소할 수 없습니다.",
                        value = CancelOrderOperationConstants.ORDER_PREPARATION_STARTED_RESPONSE),
                    @ExampleObject(
                        name = "이미 취소된 결제입니다.",
                        value = CancelOrderOperationConstants.PAYMENT_ALREADY_CANCELED_RESPONSE),
                    @ExampleObject(
                        name = "결제 취소 요청이 거부되었습니다.",
                        value = CancelOrderOperationConstants.PAYMENT_CANCELLATION_FAILED_RESPONSE)
                  }))
    })
public @interface CancelOrderOperation {}

final class CancelOrderOperationConstants {
  static final String SUMMARY = "주문 취소 API";
  static final String DESCRIPTION =
      """
     - 사용자가 주문을 취소할 수 있는 API입니다.
       - 메서드: PATCH
       - 경로: /api/orders/{id}
       - 권한: 모든 역할 접근 가능 (역할별 권한 제한)
       - 파라미터: id (주문 UUID), 요청 바디
       - 응답: 200 OK

     역할별 접근 제어
     - 역할: CUSTOMER
       - 취소 가능 범위: 자신의 주문만 취소 가능
     - 역할: OWNER
       - 취소 가능 범위: 자신의 매장 주문만 취소 가능
     - 역할: MANAGER, MASTER
       - 취소 가능 범위: 모든 주문 취소 가능
     """;
  static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  static final String MEDIA_TYPE = "application/json";
  // == 200 OK 응답 ==
  static final String SUCCESS_RESPONSE_CODE = "200";
  static final String SUCCESS_EXAMPLE =
      """
                              {
                                "message": "주문이 성공적으로 취소되었습니다."
                              }
                              """;
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

  // == 에러 응답 예시 ==
  static final String NOT_FOUND_ORDER_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "404",
                              "errorCode": "NOT_FOUND_ORDER",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "해당 주문을 찾을 수 없습니다.",
                              "path": "/api/v1/orders/{id}"
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
                              "path": "/api/v1/orders/{id}"
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
                              "path": "/api/v1/orders/{id}"
                            }
                        """;
  static final String ORDER_CANCEL_PERMISSION_DENIED_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "403",
                              "errorCode": "ORDER_CANCEL_PERMISSION_DENIED",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "주문 취소 권한이 없습니다.",
                              "path": "/api/v1/orders/{id}"
                            }
                        """;
  static final String ORDER_ALREADY_CANCELED_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "400",
                              "errorCode": "ORDER_ALREADY_CANCELED",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "이미 취소된 주문입니다.",
                              "path": "/api/v1/orders/{id}"
                            }
                        """;
  static final String ORDER_NOT_CANCELABLE_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "400",
                              "errorCode": "ORDER_NOT_CANCELABLE",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "취소할 수 없는 주문입니다.",
                              "path": "/api/v1/orders/{id}"
                            }
                        """;
  static final String ORDER_CANCELLATION_TIMEOUT_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "400",
                              "errorCode": "ORDER_CANCELLATION_TIMEOUT",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "주문 생성 후 5분이 경과하여 취소할 수 없습니다.",
                              "path": "/api/v1/orders/{id}"
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
                              "path": "/api/v1/orders/{id}"
                            }
                        """;
  static final String ORDER_ALREADY_COMPLETED_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "409",
                              "errorCode": "ORDER_ALREADY_COMPLETED",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "완료된 주문은 취소할 수 없습니다.",
                              "path": "/api/v1/orders/{id}"
                            }
                        """;
  static final String ORDER_PREPARATION_STARTED_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "409",
                              "errorCode": "ORDER_PREPARATION_STARTED",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "음식 준비가 시작된 주문은 취소할 수 없습니다.",
                              "path": "/api/v1/orders/{id}"
                            }
                        """;
  static final String PAYMENT_ALREADY_CANCELED_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "409",
                              "errorCode": "PAYMENT_ALREADY_CANCELED",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "이미 취소된 결제입니다.",
                              "path": "/api/v1/orders/{id}"
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
                              "path": "/api/v1/orders/{id}"
                            }
                        """;
  // 추가된 요청 본문 검증 에러 응답
  static final String ORDER_REASON_NULL_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "400",
                              "errorCode": "ORDER_REASON_NULL",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "취소 이유가 null일 수 없습니다.",
                              "path": "/api/v1/orders/{id}"
                            }
                        """;

  private CancelOrderOperationConstants() {}
}
