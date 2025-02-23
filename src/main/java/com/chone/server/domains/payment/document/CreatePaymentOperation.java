package com.chone.server.domains.payment.document;

import com.chone.server.domains.payment.dto.response.CreatePaymentResponse;
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
    summary = CreatePaymentOperationConstants.SUMMARY,
    description = CreatePaymentOperationConstants.DESCRIPTION)
@SecurityRequirement(name = CreatePaymentOperationConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = CreatePaymentOperationConstants.SUCCESS_RESPONSE_CODE,
          description = "결제 생성 성공",
          content =
              @Content(
                  mediaType = CreatePaymentOperationConstants.MEDIA_TYPE,
                  schema = @Schema(implementation = CreatePaymentResponse.class),
                  examples =
                      @ExampleObject(value = CreatePaymentOperationConstants.SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = "401",
          description = "인증이 필요함",
          content =
              @Content(
                  mediaType = CreatePaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "인증이 필요함",
                        value = CreatePaymentOperationConstants.UNAUTHORIZED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "404",
          description = "주문을 찾을 수 없음",
          content =
              @Content(
                  mediaType = CreatePaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "해당 주문을 찾을 수 없습니다.",
                        value = CreatePaymentOperationConstants.NOT_FOUND_ORDER_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "409",
          description = "이미 결제가 진행된 주문입니다.",
          content =
              @Content(
                  mediaType = CreatePaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "이미 결제가 진행된 주문입니다.",
                        value = CreatePaymentOperationConstants.ALREADY_PAID_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청",
          content =
              @Content(
                  mediaType = CreatePaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "취소된 주문은 결제할 수 없습니다.",
                        value = CreatePaymentOperationConstants.CANCELED_ORDER_RESPONSE),
                    @ExampleObject(
                        name = "결제 금액이 주문 금액과 일치하지 않습니다.",
                        value = CreatePaymentOperationConstants.PRICE_MISMATCH_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "403",
          description = "접근 권한이 없음",
          content =
              @Content(
                  mediaType = CreatePaymentOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "고객은 현장 결제에 대한 결제 권한이 없습니다.",
                        value =
                            CreatePaymentOperationConstants.NOT_ALLOW_PAY_ONLINE_ORDER_RESPONSE),
                    @ExampleObject(
                        name = "주문자만 결제할 수 있습니다.",
                        value = CreatePaymentOperationConstants.NOT_CUSTOMER_PAYMENT_RESPONSE)
                  }))
    })
public @interface CreatePaymentOperation {}

final class CreatePaymentOperationConstants {
  static final String SUMMARY = "결제 생성 API";
  static final String DESCRIPTION =
      """
- 주문에 대한 결제를 생성하는 API입니다.
    - 메서드: **POST**
    - 경로: /api/v1/payments
    - 권한: `CUSTOMER`, `OWNER`, `MANAGER`, `MASTER` 역할 접근 가능
    - 요청: `CreatePaymentRequest` (검증 어노테이션 적용)
    - 응답: 201 Created
    - Location 헤더: 생성된 결제 리소스 URI
""";
  static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  static final String MEDIA_TYPE = "application/json";

  // == 201 Created 응답 ==
  static final String SUCCESS_RESPONSE_CODE = "201";
  static final String SUCCESS_EXAMPLE =
      """
            {
              "id": "123e4567-e89b-12d3-a456-426614174000",
              "orderId": "123e4567-e89b-12d3-a456-426614174000",
              "totalPrice": 15000,
              "status": "COMPLETED",
              "paymentMethod": "CARD",
              "createdAt": "2024-02-12T15:30:00"
            }
            """;

  // == 에러 응답 예시 ==
  static final String UNAUTHORIZED_RESPONSE =
      """
            {
                "httpMethod": "POST",
                "httpStatus": "401",
                "errorCode": "UNAUTHORIZED",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                "path": "/api/v1/payments"
            }
            """;

  static final String NOT_FOUND_ORDER_RESPONSE =
      """
            {
                "httpMethod": "POST",
                "httpStatus": "404",
                "errorCode": "NOT_FOUND_ORDER",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "해당 주문을 찾을 수 없습니다.",
                "path": "/api/v1/payments"
            }
            """;

  static final String ALREADY_PAID_RESPONSE =
      """
            {
                "httpMethod": "POST",
                "httpStatus": "409",
                "errorCode": "ALREADY_PAID",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "이미 결제가 진행된 주문입니다.",
                "path": "/api/v1/payments"
            }
            """;

  static final String CANCELED_ORDER_RESPONSE =
      """
            {
                "httpMethod": "POST",
                "httpStatus": "400",
                "errorCode": "CANCELED_ORDER",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "취소된 주문은 결제할 수 없습니다.",
                "path": "/api/v1/payments"
            }
            """;

  static final String PRICE_MISMATCH_RESPONSE =
      """
            {
                "httpMethod": "POST",
                "httpStatus": "400",
                "errorCode": "PRICE_MISMATCH",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "결제 금액이 주문 금액과 일치하지 않습니다.",
                "path": "/api/v1/payments"
            }
            """;

  static final String NOT_ALLOW_PAY_ONLINE_ORDER_RESPONSE =
      """
            {
                "httpMethod": "POST",
                "httpStatus": "403",
                "errorCode": "NOT_ALLOW_PAY_ONLINE_ORDER",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "고객은 현장 결제에 대한 결제 권한이 없습니다.",
                "path": "/api/v1/payments"
            }
            """;

  static final String NOT_CUSTOMER_PAYMENT_RESPONSE =
      """
            {
                "httpMethod": "POST",
                "httpStatus": "403",
                "errorCode": "NOT_CUSTOMER_PAYMENT",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "주문자만 결제할 수 있습니다.",
                "path": "/api/v1/payments"
            }
            """;
}
