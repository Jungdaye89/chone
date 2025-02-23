package com.chone.server.domains.order.document;

import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.BAD_REQUEST_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_BAD_REQUEST;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_CONFLICT;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_FORBIDDEN;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_NOT_FOUND;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_OK;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_UNAUTHORIZED;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.FORBIDDEN_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.MEDIA_TYPE;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.ORDER_NOT_FOUND_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.SECURITY_REQUIREMENT;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.UNAUTHORIZED_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.CANCEL_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.CANCEL_SUMMARY;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.FAILED_PAYMENT_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.FAILED_PAYMENT_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.NOT_FOUND_ORDER_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_ALREADY_CANCELED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_ALREADY_CANCELED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_ALREADY_COMPLETED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_ALREADY_COMPLETED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_CANCELLATION_TIMEOUT_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_CANCELLATION_TIMEOUT_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_CANCEL_PERMISSION_DENIED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_CANCEL_PERMISSION_DENIED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_CUSTOMER_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_NOT_CANCELABLE_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_NOT_CANCELABLE_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_PREPARATION_STARTED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_PREPARATION_STARTED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_REASON_NULL_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.ORDER_STORE_OWNER_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.PAYMENT_ALREADY_CANCELED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.PAYMENT_ALREADY_CANCELED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.PAYMENT_CANCELLATION_FAILED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.PAYMENT_CANCELLATION_FAILED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.SUCCESS_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.SUCCESS_EXAMPLE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Cancel.UNAUTHORIZED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.ORDER_CUSTOMER_ACCESS_DENIED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.ORDER_NOT_FOUND_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.ORDER_STORE_OWNER_ACCESS_DENIED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UNAUTHORIZED_NAME;

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
@Operation(summary = CANCEL_SUMMARY, description = CANCEL_DESCRIPTION)
@SecurityRequirement(name = SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = CODE_OK,
          description = SUCCESS_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  schema = @Schema(implementation = CancelOrderResponse.class),
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
          responseCode = CODE_FORBIDDEN,
          description = FORBIDDEN_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = ORDER_CUSTOMER_ACCESS_DENIED_NAME,
                        value = ORDER_CUSTOMER_ACCESS_DENIED_VALUE),
                    @ExampleObject(
                        name = ORDER_STORE_OWNER_ACCESS_DENIED_NAME,
                        value = ORDER_STORE_OWNER_ACCESS_DENIED_VALUE),
                    @ExampleObject(
                        name = ORDER_CANCEL_PERMISSION_DENIED_NAME,
                        value = ORDER_CANCEL_PERMISSION_DENIED_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_BAD_REQUEST,
          description = BAD_REQUEST_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = ORDER_ALREADY_CANCELED_NAME,
                        value = ORDER_ALREADY_CANCELED_VALUE),
                    @ExampleObject(
                        name = ORDER_NOT_CANCELABLE_NAME,
                        value = ORDER_NOT_CANCELABLE_VALUE),
                    @ExampleObject(
                        name = ORDER_CANCELLATION_TIMEOUT_NAME,
                        value = ORDER_CANCELLATION_TIMEOUT_VALUE),
                    @ExampleObject(name = FAILED_PAYMENT_NAME, value = FAILED_PAYMENT_VALUE),
                    @ExampleObject(
                        name = ORDER_REASON_NULL_NAME,
                        value = ORDER_ALREADY_COMPLETED_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_CONFLICT,
          description = "충돌 발생",
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = ORDER_ALREADY_COMPLETED_NAME,
                        value = ORDER_ALREADY_COMPLETED_VALUE),
                    @ExampleObject(
                        name = ORDER_PREPARATION_STARTED_NAME,
                        value = ORDER_PREPARATION_STARTED_VALUE),
                    @ExampleObject(
                        name = PAYMENT_ALREADY_CANCELED_NAME,
                        value = PAYMENT_ALREADY_CANCELED_VALUE),
                    @ExampleObject(
                        name = PAYMENT_CANCELLATION_FAILED_NAME,
                        value = PAYMENT_CANCELLATION_FAILED_VALUE)
                  }))
    })
public @interface CancelOrderOperation {}
