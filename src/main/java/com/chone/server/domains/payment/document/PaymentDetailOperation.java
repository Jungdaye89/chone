package com.chone.server.domains.payment.document;

import com.chone.server.domains.payment.dto.response.PaymentDetailResponse;
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
    summary = PaymentDetailOperationConstants.SUMMARY,
    description = PaymentDetailOperationConstants.DESCRIPTION)
@SecurityRequirement(name = PaymentDetailOperationConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = "200",
          description = "결제 내역 조회 성공",
          content =
              @Content(
                  mediaType = PaymentDetailOperationConstants.MEDIA_TYPE,
                  schema = @Schema(implementation = PaymentDetailResponse.class),
                  examples =
                      @ExampleObject(value = PaymentDetailOperationConstants.SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = "401",
          description = "인증이 필요함",
          content =
              @Content(
                  mediaType = PaymentDetailOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "인증이 필요함",
                        value = PaymentDetailOperationConstants.UNAUTHORIZED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "404",
          description = "결제 내역을 찾을 수 없음",
          content =
              @Content(
                  mediaType = PaymentDetailOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "해당 결제 내역을 찾을 수 없습니다.",
                        value = PaymentDetailOperationConstants.NOT_FOUND_PAYMENT_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "403",
          description = "접근 권한이 없음",
          content =
              @Content(
                  mediaType = PaymentDetailOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "주문자만 결제 내역을 조회할 수 있습니다.",
                        value =
                            PaymentDetailOperationConstants.NOT_CUSTOMER_PAYMENT_HISTORY_RESPONSE),
                    @ExampleObject(
                        name = "해당 가게의 주인만 결제 내역을 조회할 수 있습니다.",
                        value = PaymentDetailOperationConstants.NOT_OWNER_PAYMENT_HISTORY_RESPONSE)
                  }))
    })
public @interface PaymentDetailOperation {}

final class PaymentDetailOperationConstants {
  static final String SUMMARY = "결제 내역 조회 API";
  static final String DESCRIPTION =
      """
- 결제 ID에 대한 결제 내역을 조회하는 API입니다.
- 메서드: **GET**
- 경로: /api/payments/{id}
- 권한: 모든 역할 접근 가능 (역할별 조회 범위 제한)
- 파라미터: id (결제 UUID)
- 응답: 200 OK

- 역할: 조회 가능 범위
    - CUSTOMER: 자신의 결제만 조회
    - OWNER: 자신의 매장 결제만 조회
    - MANAGER, MASTER: 모든 결제 조회
""";
  static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  static final String MEDIA_TYPE = "application/json";

  // == 200 OK 응답 ==
  static final String SUCCESS_EXAMPLE =
      """
            {
              "payment": {
                "id": "123e4567-e89b-12d3-a456-426614174000",
                "status": "COMPLETED",
                "cancelReason": null,
                "method": "카드"
              },
              "order": {
                "id": "123e4567-e89b-12d3-a456-426614174001",
                "totalPrice": 15000,
                "type": "포장",
                "userId": 12345,
                "storeId": "123e4567-e89b-12d3-a456-426614174002",
                "storeName": "맛있는 식당"
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

  static final String NOT_FOUND_PAYMENT_RESPONSE =
      """
            {
                "httpMethod": "GET",
                "httpStatus": "404",
                "errorCode": "NOT_FOUND_PAYMENT",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "해당 결제 내역을 찾을 수 없습니다.",
                "path": "/api/v1/payments/{id}"
            }
            """;

  static final String NOT_CUSTOMER_PAYMENT_HISTORY_RESPONSE =
      """
            {
                "httpMethod": "GET",
                "httpStatus": "403",
                "errorCode": "NOT_CUSTOMER_PAYMENT_HISTORY",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "주문자만 결제 내역을 조회할 수 있습니다.",
                "path": "/api/v1/payments/{id}"
            }
            """;

  static final String NOT_OWNER_PAYMENT_HISTORY_RESPONSE =
      """
            {
                "httpMethod": "GET",
                "httpStatus": "403",
                "errorCode": "NOT_OWNER_PAYMENT_HISTORY",
                "timestamp": "2025-02-23T15:32:45.123456",
                "message": "해당 가게의 주인만 결제 내역을 조회할 수 있습니다.",
                "path": "/api/v1/payments/{id}"
            }
            """;
}
