package com.chone.server.domains.order.document;

import com.chone.server.domains.order.dto.response.CreateOrderResponse;
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
    summary = CreateOrderOperationConstants.SUMMARY,
    description = CreateOrderOperationConstants.DESCRIPTION)
@SecurityRequirement(name = CreateOrderOperationConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = CreateOrderOperationConstants.SUCCESS_RESPONSE_CODE,
          description = "주문 생성 성공",
          content =
              @Content(
                  mediaType = CreateOrderOperationConstants.MEDIA_TYPE,
                  schema = @Schema(implementation = CreateOrderResponse.class),
                  examples =
                      @ExampleObject(value = CreateOrderOperationConstants.SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = "401",
          description = "인증이 필요함",
          content =
              @Content(
                  mediaType = CreateOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "인증이 필요함",
                        value = CreateOrderOperationConstants.UNAUTHORIZED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청",
          content =
              @Content(
                  mediaType = CreateOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "가게 ID는 필수입니다",
                        value = CreateOrderOperationConstants.MISSING_STORE_ID_RESPONSE),
                    @ExampleObject(
                        name = "주문 상품은 최소 1개 이상이어야 합니다",
                        value = CreateOrderOperationConstants.MISSING_ORDER_ITEMS_RESPONSE),
                    @ExampleObject(
                        name = "존재하지 않는 가게입니다",
                        value = CreateOrderOperationConstants.STORE_NOT_FOUND_RESPONSE),
                    @ExampleObject(
                        name = "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.",
                        value =
                            CreateOrderOperationConstants.ORDER_STORE_OWNER_ACCESS_DENIED_RESPONSE),
                    @ExampleObject(
                        name = "주문 상품은 동일한 가게의 상품이어야 합니다.",
                        value = CreateOrderOperationConstants.MULTIPLE_STORE_ORDER_RESPONSE),
                    @ExampleObject(
                        name = "배송지 주소는 필수입니다",
                        value = CreateOrderOperationConstants.MISSING_DELIVERY_ADDRESS_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "403",
          description = "접근 권한이 없음",
          content =
              @Content(
                  mediaType = CreateOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.",
                        value =
                            CreateOrderOperationConstants.ORDER_STORE_OWNER_ACCESS_DENIED_RESPONSE)
                  }))
    })
public @interface CreateOrderOperation {}

final class CreateOrderOperationConstants {
  static final String SUMMARY = "주문 생성 API";
  static final String DESCRIPTION =
      """
        - 사용자가 주문을 생성할 수 있는 API입니다.
          - 메서드: POST
          - 경로: /api/v1/orders
          - 권한: CUSTOMER, STORE_OWNER 역할만 접근 가능
          - 응답: 201 Created
          - Location 헤더: 생성된 리소스 URI
      """;
  static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  static final String MEDIA_TYPE = "application/json";
  // == 200 OK 응답 ==
  static final String SUCCESS_RESPONSE_CODE = "201";
  static final String SUCCESS_EXAMPLE =
      """
            {
                "id": "3ec24ba4-0164-45d1-9566-73dde029b2c1",
                "status": "CREATED",
                "message": "주문이 성공적으로 생성되었습니다."
            }
        """;
  static final String UNAUTHORIZED_RESPONSE =
      """
                            {
                              "httpMethod": "POST",
                              "httpStatus": "401",
                              "errorCode": "UNAUTHORIZED",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                              "path": "/api/v1/orders"
                            }
                        """;

  // == 에러 응답 예시 ==
  static final String MISSING_STORE_ID_RESPONSE =
      """
                            {
                              "httpMethod": "POST",
                              "httpStatus": "400",
                              "errorCode": "INVALID_INPUT",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "가게 ID는 필수입니다.",
                              "details": {
                                  "storeId": "가게 ID는 필수입니다"
                              },
                              "path": "/api/v1/orders"
                            }
                        """;
  static final String MISSING_ORDER_ITEMS_RESPONSE =
      """
                            {
                              "httpMethod": "POST",
                              "httpStatus": "400",
                              "errorCode": "INVALID_INPUT",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "주문 상품은 최소 1개 이상이어야 합니다.",
                              "details": {
                                  "orderItems": "주문 상품은 최소 1개 이상이어야 합니다"
                              },
                              "path": "/api/v1/orders"
                            }
                        """;
  static final String STORE_NOT_FOUND_RESPONSE =
      """
                            {
                              "httpMethod": "POST",
                              "httpStatus": "404",
                              "errorCode": "STORE_NOT_FOUND",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "존재하지 않는 가게입니다.",
                              "path": "/api/v1/orders"
                            }
                        """;
  static final String MULTIPLE_STORE_ORDER_RESPONSE =
      """
                            {
                              "httpMethod": "POST",
                              "httpStatus": "400",
                              "errorCode": "MULTIPLE_STORE_ORDER",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "주문 상품은 동일한 가게의 상품이어야 합니다.",
                              "path": "/api/v1/orders"
                            }
                        """;
  static final String MISSING_DELIVERY_ADDRESS_RESPONSE =
      """
                            {
                              "httpMethod": "POST",
                              "httpStatus": "400",
                              "errorCode": "MISSING_DELIVERY_ADDRESS",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "배송지 주소는 필수입니다.",
                              "path": "/api/v1/orders"
                            }
                        """;
  static final String ORDER_STORE_OWNER_ACCESS_DENIED_RESPONSE =
      """
                            {
                              "httpMethod": "POST",
                              "httpStatus": "403",
                              "errorCode": "ORDER_STORE_OWNER_ACCESS_DENIED",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.",
                              "path": "/api/v1/orders"
                            }
                        """;

  private CreateOrderOperationConstants() {}
}
