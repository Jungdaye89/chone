package com.chone.server.domains.order.document;

import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_FORBIDDEN;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_OK;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_UNAUTHORIZED;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.MEDIA_TYPE;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.SECURITY_REQUIREMENT;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.LIST_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.LIST_SUMMARY;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.FORBIDDEN_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.List.CUSTOMER_ORDER_FILTERING_ACCESS_DENIED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.List.CUSTOMER_ORDER_FILTERING_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.List.STORE_ORDER_FILTERING_ACCESS_DENIED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.List.STORE_ORDER_FILTERING_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.List.SUCCESS_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.List.SUCCESS_EXAMPLE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.List.UNAUTHORIZED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UNAUTHORIZED_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UNAUTHORIZED_NAME;

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
    summary = LIST_SUMMARY,
    description = LIST_DESCRIPTION,
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
@SecurityRequirement(name = SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = CODE_OK,
          description = SUCCESS_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  schema = @Schema(implementation = PageResponse.class),
                  examples = @ExampleObject(value = SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = CODE_UNAUTHORIZED,
          description = UNAUTHORIZED_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(name = UNAUTHORIZED_NAME, value = UNAUTHORIZED_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_FORBIDDEN,
          description = FORBIDDEN_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = CUSTOMER_ORDER_FILTERING_ACCESS_DENIED_NAME,
                        value = CUSTOMER_ORDER_FILTERING_ACCESS_DENIED_VALUE),
                    @ExampleObject(
                        name = STORE_ORDER_FILTERING_ACCESS_DENIED_NAME,
                        value = STORE_ORDER_FILTERING_ACCESS_DENIED_VALUE)
                  }))
    })
public @interface OrderListOperation {}
