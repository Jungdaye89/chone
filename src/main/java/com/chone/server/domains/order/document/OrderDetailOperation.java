package com.chone.server.domains.order.document;

import com.chone.server.domains.order.dto.response.OrderDetailResponse;
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
    summary = OrderDetailOperationConstants.SUMMARY,
    description = OrderDetailOperationConstants.DESCRIPTION)
@SecurityRequirement(name = OrderDetailOperationConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = OrderDetailOperationConstants.SUCCESS_RESPONSE_CODE,
          description = "주문 상세 조회 성공",
          content =
              @Content(
                  mediaType = OrderDetailOperationConstants.MEDIA_TYPE,
                  schema = @Schema(implementation = OrderDetailResponse.class),
                  examples =
                      @ExampleObject(value = OrderDetailOperationConstants.SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = "403",
          description = "주문 상세 보기에 권한이 없음",
          content =
              @Content(
                  mediaType = OrderDetailOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "해당 주문의 소유자가 아니라 CUSTOMER 접근 권한 없음",
                        value =
                            OrderDetailOperationConstants.ORDER_CUSTOMER_ACCESS_DENIED_RESPONSE),
                    @ExampleObject(
                        name = "해당 매장의 소유자가 아니라 STORE OWNER 접근 권한 없음",
                        value =
                            OrderDetailOperationConstants.ORDER_STORE_OWNER_ACCESS_DENIED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "404",
          description = "주문을 찾을 수 없음",
          content =
              @Content(
                  mediaType = OrderDetailOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "해당 주문을 찾을 수 없음",
                        value = OrderDetailOperationConstants.NOT_FOUND_ORDER_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "401",
          description = "인증이 필요함",
          content =
              @Content(
                  mediaType = OrderDetailOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "인증이 필요함",
                        value = OrderDetailOperationConstants.UNAUTHORIZED_RESPONSE)
                  }))
    })
public @interface OrderDetailOperation {}

final class OrderDetailOperationConstants {
  static final String SUMMARY = "주문 상세 조회 API";
  static final String DESCRIPTION =
      """
      - 사용자 역할(고객, 가게 주인, 관리자)에 따라 주문 상세 정보를 조회할 수 있습니다.
        - 메서드: GET
        - 경로: /api/orders/{id}
        - 권한: 모든 역할 접근 가능 (역할별 조회 범위 제한)
        - 파라미터: id (주문 UUID)
        - 응답: 200 OK

      역할별 접근 제어
      - 역할: CUSTOMER
        - 조회 가능 범위: 자신의 주문만 조회
      - 역할: OWNER
        - 조회 가능 범위: 자신의 매장 주문만 조회
      - 역할: MANAGER, MASTER
        - 조회 가능 범위: 모든 주문 조회
    """;
  static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  static final String MEDIA_TYPE = "application/json";
  // == 200 OK 응답 ==
  static final String SUCCESS_RESPONSE_CODE = "200";
  static final String SUCCESS_EXAMPLE =
      """
                        {
                          "order": {
                            "id": "3ec24ba4-0164-45d1-9566-73dde029b2c1",
                            "type": "OFFLINE",
                            "status": "PENDING",
                            "totalPrice": 13000,
                            "cancelReason": null,
                            "request": "포장해주세요"
                          },
                          "user": {
                            "id": 1,
                            "username": "홍길동"
                          },
                          "store": {
                            "id": "db8ad8d5-4b9e-454c-82a0-d5ca1eadc7c1",
                            "storeName": "종로카페"
                          },
                          "orderItems": [
                            {
                              "id": "5db71f2e-34a5-4232-9670-87654f761abc",
                              "productName": "아메리카노",
                              "amount": 2
                            },
                            {
                              "id": "9c8b72d1-45e2-41b3-a58c-98765c432def",
                              "productName": "치즈케이크",
                              "amount": 1
                            }
                          ]
                        }
                        """;
  static final String ORDER_CUSTOMER_ACCESS_DENIED_RESPONSE =
      """
                      {
                        "httpMethod": "GET",
                        "httpStatus": "403",
                        "errorCode": "ORDER_CUSTOMER_ACCESS_DENIED",
                        "timestamp": "2025-02-23T15:32:45.123456",
                        "message": "접근 권한이 없습니다. 해당 주문의 소유자가 아닙니다.",
                        "path": "/api/orders/3ec24ba4-0164-45d1-9566-73dde029b2c1"
                      }
                  """;

  // == 에러 응답 예시 ==
  static final String ORDER_STORE_OWNER_ACCESS_DENIED_RESPONSE =
      """
                      {
                        "httpMethod": "GET",
                        "httpStatus": "403",
                        "errorCode": "ORDER_STORE_OWNER_ACCESS_DENIED",
                        "timestamp": "2025-02-23T15:32:45.123456",
                        "message": "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.",
                        "path": "/api/orders/3ec24ba4-0164-45d1-9566-73dde029b2c1"
                      }
                  """;
  static final String NOT_FOUND_ORDER_RESPONSE =
      """
                      {
                        "httpMethod": "GET",
                        "httpStatus": "404",
                        "errorCode": "NOT_FOUND_ORDER",
                        "timestamp": "2025-02-23T15:32:45.123456",
                        "message": "해당 주문을 찾을 수 없습니다.",
                        "path": "/api/orders/3ec24ba4-0164-45d1-9566-73dde029b2c1"
                      }
                  """;
  static final String UNAUTHORIZED_RESPONSE =
      """
                        {
                          "httpMethod": "GET",
                          "httpStatus": "401",
                          "errorCode": "UNAUTHORIZED",
                          "timestamp": "2025-02-23T15:32:45.123456",
                          "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                          "path": "/api/orders/3ec24ba4-0164-45d1-9566-73dde029b2c1"
                        }
                    """;
  private OrderDetailOperationConstants() {}
}
