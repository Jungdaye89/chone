package com.chone.server.domains.order.document;

import com.chone.server.domains.order.dto.response.OrderStatusUpdateResponse;
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
    summary = UpdateOrderStatusOperationConstants.SUMMARY,
    description = UpdateOrderStatusOperationConstants.DESCRIPTION)
@SecurityRequirement(name = UpdateOrderStatusOperationConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = UpdateOrderStatusOperationConstants.SUCCESS_RESPONSE_CODE,
          description = "주문 상태 업데이트 성공",
          content =
              @Content(
                  mediaType = UpdateOrderStatusOperationConstants.MEDIA_TYPE,
                  schema = @Schema(implementation = OrderStatusUpdateResponse.class),
                  examples =
                      @ExampleObject(value = UpdateOrderStatusOperationConstants.SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = "401",
          description = "인증이 필요함",
          content =
              @Content(
                  mediaType = UpdateOrderStatusOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "인증이 필요함",
                        value = UpdateOrderStatusOperationConstants.UNAUTHORIZED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "404",
          description = "주문을 찾을 수 없음",
          content =
              @Content(
                  mediaType = UpdateOrderStatusOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "해당 주문을 찾을 수 없습니다.",
                        value = UpdateOrderStatusOperationConstants.NOT_FOUND_ORDER_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "403",
          description = "접근 권한이 없음",
          content =
              @Content(
                  mediaType = UpdateOrderStatusOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "본인 가게의 주문만 상태를 변경할 수 있습니다.",
                        value =
                            UpdateOrderStatusOperationConstants
                                .ORDER_STATUS_CHANGE_NOT_OWNER_RESPONSE),
                    @ExampleObject(
                        name = "고객은 주문 상태를 변경할 수 없습니다.",
                        value =
                            UpdateOrderStatusOperationConstants
                                .ORDER_STATUS_CHANGE_FORBIDDEN_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "400",
          description = "잘못된 요청",
          content =
              @Content(
                  mediaType = UpdateOrderStatusOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "주문 취소는 별도의 취소 요청(/api/v1/orders/{id} PATCH) 통해서만 가능합니다.",
                        value =
                            UpdateOrderStatusOperationConstants.ORDER_CANCEL_SEPARATE_API_RESPONSE),
                    @ExampleObject(
                        name = "주문 결제 완료는 결제 요청 후 변경 가능한 상태입니다.",
                        value = UpdateOrderStatusOperationConstants.ORDER_PAID_SEPARATE_RESPONSE),
                    @ExampleObject(
                        name = "주문 상태가 null일 수 없습니다.",
                        value = UpdateOrderStatusOperationConstants.ORDER_STATUS_NULL_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "409",
          description = "충돌 발생",
          content =
              @Content(
                  mediaType = UpdateOrderStatusOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "취소되거나, 완료된 주문의 상태를 변경할 수 없습니다.",
                        value =
                            UpdateOrderStatusOperationConstants
                                .ORDER_FINALIZED_STATE_CONFLICT_RESPONSE),
                    @ExampleObject(
                        name = "주문 상태를 이전 단계로 변경할 수 없습니다.",
                        value =
                            UpdateOrderStatusOperationConstants.ORDER_STATUS_REGRESSION_RESPONSE),
                    @ExampleObject(
                        name = "매장 주문은 배달 관련 상태로 변경할 수 없습니다.",
                        value =
                            UpdateOrderStatusOperationConstants
                                .OFFLINE_ORDER_DELIVERY_STATUS_RESPONSE)
                  }))
    })
public @interface UpdateOrderStatusOperation {}

final class UpdateOrderStatusOperationConstants {
  static final String SUMMARY = "주문 상태 업데이트 API";
  static final String DESCRIPTION =
      """
       - 사용자가 주문 상태를 업데이트할 수 있는 API입니다.
         - 메서드: PATCH
         - 경로: /api/v1/orders/{id}/status
         - 권한: CUSTOMER 역할 제외, 가게 주인은 본인 가게 주문만 가능
         - 응답: 200 OK
    """;
  static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  static final String MEDIA_TYPE = "application/json";
  // == 200 OK 응답 ==
  static final String SUCCESS_RESPONSE_CODE = "200";
  static final String SUCCESS_EXAMPLE =
      """
        {
          "orderId": "123e4567-e89b-12d3-a456-426614174000",
          "type": "온라인 주문",
          "status": "음식 준비 중",
          "updatedAt": "2024-02-12T15:30:00"
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
                              "path": "/api/v1/orders/{id}/status"
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
                              "path": "/api/v1/orders/{id}/status"
                            }
                        """;
  static final String ORDER_STATUS_CHANGE_NOT_OWNER_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "403",
                              "errorCode": "ORDER_STATUS_CHANGE_NOT_OWNER",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "본인 가게의 주문만 상태를 변경할 수 있습니다.",
                              "path": "/api/v1/orders/{id}/status"
                            }
                        """;
  static final String ORDER_STATUS_CHANGE_FORBIDDEN_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "403",
                              "errorCode": "ORDER_STATUS_CHANGE_FORBIDDEN",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "고객은 주문 상태를 변경할 수 없습니다.",
                              "path": "/api/v1/orders/{id}/status"
                            }
                        """;
  static final String ORDER_CANCEL_SEPARATE_API_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "400",
                              "errorCode": "ORDER_CANCEL_SEPARATE_API",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "주문 취소는 별도의 취소 요청(/api/v1/orders/{id} PATCH) 통해서만 가능합니다.",
                              "path": "/api/v1/orders/{id}/status"
                            }
                        """;
  static final String ORDER_PAID_SEPARATE_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "400",
                              "errorCode": "ORDER_PAID_SEPARATE",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "주문 결제 완료는 결제 요청 후 변경 가능한 상태입니다.",
                              "path": "/api/v1/orders/{id}/status"
                            }
                        """;
  static final String ORDER_STATUS_NULL_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "400",
                              "errorCode": "ORDER_STATUS_NULL",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "주문 상태가 null일 수 없습니다.",
                              "path": "/api/v1/orders/{id}/status"
                            }
                        """;
  static final String ORDER_FINALIZED_STATE_CONFLICT_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "409",
                              "errorCode": "ORDER_FINALIZED_STATE_CONFLICT",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "취소되거나, 완료된 주문의 상태를 변경할 수 없습니다.",
                              "path": "/api/v1/orders/{id}/status"
                            }
                        """;
  static final String ORDER_STATUS_REGRESSION_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "409",
                              "errorCode": "ORDER_STATUS_REGRESSION",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "주문 상태를 이전 단계로 변경할 수
                        "주문 상태를 이전 단계로 변경할 수 없습니다.",
                        "path": "/api/v1/orders/{id}/status"
                      }
                  """;
  static final String OFFLINE_ORDER_DELIVERY_STATUS_RESPONSE =
      """
                            {
                              "httpMethod": "PATCH",
                              "httpStatus": "409",
                              "errorCode": "OFFLINE_ORDER_DELIVERY_STATUS",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "매장 주문은 배달 관련 상태로 변경할 수 없습니다.",
                              "path": "/api/v1/orders/{id}/status"
                            }
                        """;

  private UpdateOrderStatusOperationConstants() {}
}
