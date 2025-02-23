package com.chone.server.domains.order.document;

import com.chone.server.domains.order.dto.request.OrderFilterParams;
import com.chone.server.domains.order.dto.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
    summary = OrderListOperationConstants.SUMMARY,
    description = OrderListOperationConstants.DESCRIPTION,
    parameters = {
      @Parameter(
          name = "filterParams",
          description = "주문 필터링 파라미터",
          in = ParameterIn.QUERY,
          schema = @Schema(implementation = OrderFilterParams.class)),
      @Parameter(
          name = "page",
          description = "페이지 번호 (0부터 시작)",
          in = ParameterIn.QUERY,
          schema = @Schema(type = "integer", defaultValue = "0")),
      @Parameter(
          name = "size",
          description = "페이지 크기",
          in = ParameterIn.QUERY,
          schema = @Schema(type = "integer", defaultValue = "10")),
      @Parameter(
          name = "sort",
          description = "정렬 기준 (예: createdat,desc)",
          in = ParameterIn.QUERY,
          schema = @Schema(type = "string", defaultValue = "createdat,desc"),
          examples = {
            @ExampleObject(value = "createdat,desc"),
            @ExampleObject(value = "createdat,asc"),
            @ExampleObject(value = "updatedat,desc"),
            @ExampleObject(value = "updatedat,asc"),
            @ExampleObject(value = "totalprice,desc"),
            @ExampleObject(value = "totalprice,asc")
          })
    })
@SecurityRequirement(name = OrderListOperationConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = OrderListOperationConstants.SUCCESS_RESPONSE_CODE,
          description = "주문 조회 성공",
          content =
              @Content(
                  mediaType = OrderListOperationConstants.MEDIA_TYPE,
                  schema = @Schema(implementation = PageResponse.class),
                  examples = @ExampleObject(value = OrderListOperationConstants.SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = "401",
          description = "인증이 필요함",
          content =
              @Content(
                  mediaType = OrderListOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "인증이 필요함",
                        value = OrderListOperationConstants.UNAUTHORIZED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "403",
          description = "접근 권한이 없음",
          content =
              @Content(
                  mediaType = OrderListOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "사용자별 주문 조회 권한이 없습니다.",
                        value =
                            OrderListOperationConstants
                                .CUSTOMER_ORDER_FILTERING_ACCESS_DENIED_RESPONSE),
                    @ExampleObject(
                        name = "가게별 주문 조회 권한이 없습니다.",
                        value =
                            OrderListOperationConstants
                                .STORE_ORDER_FILTERING_ACCESS_DENIED_RESPONSE)
                  }))
    })
public @interface OrderListOperation {}

final class OrderListOperationConstants {

  static final String SUMMARY = "주문 조회 API";
  static final String DESCRIPTION =
      """
        - 사용자별 또는 가게별로 주문을 조회할 수 있는 API입니다.
          - 메서드: GET
          - 경로: /api/v1/orders
          - 권한: 모든 역할 접근 가능 (역할별 조회 범위 제한)
          - 페이지네이션: page, size, sort 지원 (기본값: page=0, size=10, sort=createdat,desc)
          - 응답: 200 OK + PageResponse<OrderPageResponse>

        역할별 조회 가능 범위 및 필터링 제한
        - 역할: CUSTOMER
          - 조회 가능 범위: 자신의 주문만 조회
          - 필터링 제한: 사용자 필터링 불가, 가게 필터링 가능
        - 역할: OWNER
          - 조회 가능 범위: 자신의 매장 주문만 조회
          - 필터링 제한: 사용자 필터링 가능, 가게 필터링 불가
        - 역할: MANAGER, MASTER
          - 조회 가능 범위: 모든 주문 조회
          - 필터링 제한: 모든 필터링 가능
        """;
  static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  static final String MEDIA_TYPE = "application/json";

  // == 200 OK 응답 ==
  static final String SUCCESS_RESPONSE_CODE = "200";
  static final String SUCCESS_EXAMPLE =
      """
{
    "content": [
        {
            "id": "3ec24ba4-0164-45d1-9566-73dde029b2c1",
            "storeId": "db8ad8d5-4b9e-454c-82a0-d5ca1eadc7c1",
            "storeName": "종로카페",
            "type": "OFFLINE",
            "status": "PENDING",
            "totalPrice": 13000,
            "createdAt": "2025-02-18T11:52:22.289518",
            "updatedAt": "2025-02-18T11:52:22.289518"
        }
    ],
    "pageInfo": {
        "page": 0,
        "size": 10,
        "totalElements": 4,
        "totalPages": 1,
        "last": true
    }
}
""";

  // == 에러 응답 예시 ==

  static final String UNAUTHORIZED_RESPONSE =
      """
                                  {
                                    "httpMethod": "GET",
                                    "httpStatus": "401",
                                    "errorCode": "UNAUTHORIZED",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                                    "path": "/api/v1/orders"
                                  }
                              """;

  static final String CUSTOMER_ORDER_FILTERING_ACCESS_DENIED_RESPONSE =
      """
                                  {
                                    "httpMethod": "GET",
                                    "httpStatus": "403",
                                    "errorCode": "CUSTOMER_ORDER_FILTERING_ACCESS_DENIED",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "사용자별 주문 조회 권한이 없습니다.",
                                    "path": "/api/v1/orders"
                                  }
                              """;

  static final String STORE_ORDER_FILTERING_ACCESS_DENIED_RESPONSE =
      """
                                  {
                                    "httpMethod": "GET",
                                    "httpStatus": "403",
                                    "errorCode": "STORE_ORDER_FILTERING_ACCESS_DENIED",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "가게별 주문 조회 권한이 없습니다.",
                                    "path": "/api/v1/orders"
                                  }
                              """;
}
