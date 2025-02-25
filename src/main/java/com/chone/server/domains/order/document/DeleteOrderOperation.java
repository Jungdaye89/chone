package com.chone.server.domains.order.document;

import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_CONFLICT;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_NOT_FOUND;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_OK;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_UNAUTHORIZED;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.MEDIA_TYPE;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.ORDER_NOT_FOUND_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.UNAUTHORIZED_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.DELETE_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.DELETE_SUMMARY;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Delete.NOT_FOUND_ORDER_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Delete.ORDER_NOT_DELETABLE_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Delete.ORDER_NOT_DELETABLE_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Delete.SUCCESS_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Delete.SUCCESS_EXAMPLE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Delete.UNAUTHORIZED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.ORDER_NOT_FOUND_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UNAUTHORIZED_NAME;

import com.chone.server.domains.order.document.constants.OrderOperationCommonConstants;
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
@Operation(summary = DELETE_SUMMARY, description = DELETE_DESCRIPTION)
@SecurityRequirement(name = OrderOperationCommonConstants.SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = CODE_OK,
          description = SUCCESS_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  schema = @Schema(implementation = DeleteOrderResponse.class),
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
          responseCode = CODE_NOT_FOUND,
          description = ORDER_NOT_FOUND_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(name = ORDER_NOT_FOUND_NAME, value = NOT_FOUND_ORDER_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_CONFLICT,
          description = "주문 삭제 불가",
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = ORDER_NOT_DELETABLE_NAME,
                        value = ORDER_NOT_DELETABLE_VALUE)
                  }))
    })
public @interface DeleteOrderOperation {}
