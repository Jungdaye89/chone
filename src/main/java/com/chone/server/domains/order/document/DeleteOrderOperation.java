package com.chone.server.domains.order.document;

import com.chone.server.domains.order.dto.response.DeleteOrderResponse;
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
    summary = DeleteOrderOperationConstants.SUMMARY,
    description = DeleteOrderOperationConstants.DESCRIPTION)
@SecurityRequirement(name = DeleteOrderOperationConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = DeleteOrderOperationConstants.SUCCESS_RESPONSE_CODE,
          description = "주문 삭제 성공",
          content =
              @Content(
                  mediaType = DeleteOrderOperationConstants.MEDIA_TYPE,
                  schema = @Schema(implementation = DeleteOrderResponse.class),
                  examples =
                      @ExampleObject(value = DeleteOrderOperationConstants.SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = "401",
          description = "인증이 필요함",
          content =
              @Content(
                  mediaType = DeleteOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "인증이 필요함",
                        value = DeleteOrderOperationConstants.UNAUTHORIZED_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "404",
          description = "주문을 찾을 수 없음",
          content =
              @Content(
                  mediaType = DeleteOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "해당 주문을 찾을 수 없습니다.",
                        value = DeleteOrderOperationConstants.NOT_FOUND_ORDER_RESPONSE)
                  })),
      @ApiResponse(
          responseCode = "409",
          description = "주문 삭제 불가",
          content =
              @Content(
                  mediaType = DeleteOrderOperationConstants.MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = "주문 과정이 완료되지 않아 삭제할 수 없는 주문입니다.",
                        value = DeleteOrderOperationConstants.ORDER_NOT_DELETABLE_RESPONSE)
                  }))
    })
public @interface DeleteOrderOperation {}

final class DeleteOrderOperationConstants {

  static final String SUMMARY = "주문 삭제 API";
  static final String DESCRIPTION =
      """
        - 항목: 내용
          - 메서드: DELETE
          - 경로: /api/orders/{id}
          - 권한: MANAGER, MASTER 역할만 접근 가능
          - 파라미터: id (주문 UUID)
          - 응답: 200 OK

        역할별 접근 제어
        - 역할: MANAGER, MASTER
          - 삭제 가능 범위: 주문 상태가 취소 또는 완료된 주문만 삭제 가능
    """;
  static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  static final String MEDIA_TYPE = "application/json";

  // == 200 OK 응답 ==
  static final String SUCCESS_RESPONSE_CODE = "200";
  static final String SUCCESS_EXAMPLE =
      """
        {
          "deletedRequestedAt": "2024-02-19T14:30:00Z"
        }
      """;

  // == 에러 응답 예시 ==

  static final String UNAUTHORIZED_RESPONSE =
      """
                            {
                              "httpMethod": "DELETE",
                              "httpStatus": "401",
                              "errorCode": "UNAUTHORIZED",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                              "path": "/api/v1/orders/{id}"
                            }
                        """;

  static final String NOT_FOUND_ORDER_RESPONSE =
      """
                            {
                              "httpMethod": "DELETE",
                              "httpStatus": "404",
                              "errorCode": "NOT_FOUND_ORDER",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "해당 주문을 찾을 수 없습니다.",
                              "path": "/api/v1/orders/{id}"
                            }
                        """;

  static final String ORDER_NOT_DELETABLE_RESPONSE =
      """
                            {
                              "httpMethod": "DELETE",
                              "httpStatus": "409",
                              "errorCode": "ORDER_NOT_DELETABLE",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "주문 과정이 완료되지 않아 삭제할 수 없는 주문입니다.",
                              "path": "/api/v1/orders/{id}"
                            }
                        """;
}
