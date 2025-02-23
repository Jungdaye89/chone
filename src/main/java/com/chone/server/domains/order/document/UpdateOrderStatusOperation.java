package com.chone.server.domains.order.document;

import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.BAD_REQUEST_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_BAD_REQUEST;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_CONFLICT;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_FORBIDDEN;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_NOT_FOUND;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_OK;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_UNAUTHORIZED;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CONFLICT_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.FORBIDDEN_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.MEDIA_TYPE;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.ORDER_NOT_FOUND_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.SECURITY_REQUIREMENT;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.UNAUTHORIZED_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.UPDATE_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.UPDATE_STATUS_SUMMARY;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.ORDER_NOT_FOUND_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UNAUTHORIZED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.NOT_FOUND_ORDER_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.OFFLINE_ORDER_DELIVERY_STATUS_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.OFFLINE_ORDER_DELIVERY_STATUS_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_CANCEL_SEPARATE_API_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_CANCEL_SEPARATE_API_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_FINALIZED_STATE_CONFLICT_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_FINALIZED_STATE_CONFLICT_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_PAID_SEPARATE_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_PAID_SEPARATE_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_STATUS_CHANGE_FORBIDDEN_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_STATUS_CHANGE_FORBIDDEN_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_STATUS_CHANGE_NOT_OWNER_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_STATUS_CHANGE_NOT_OWNER_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_STATUS_NULL_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_STATUS_NULL_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_STATUS_REGRESSION_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.ORDER_STATUS_REGRESSION_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.SUCCESS_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.SUCCESS_EXAMPLE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UpdateStatus.UNAUTHORIZED_VALUE;

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
@Operation(summary = UPDATE_STATUS_SUMMARY, description = UPDATE_DESCRIPTION)
@SecurityRequirement(name = SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = CODE_OK,
          description = SUCCESS_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  schema = @Schema(implementation = OrderStatusUpdateResponse.class),
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
                        name = ORDER_STATUS_CHANGE_NOT_OWNER_NAME,
                        value = ORDER_STATUS_CHANGE_NOT_OWNER_VALUE),
                    @ExampleObject(
                        name = ORDER_STATUS_CHANGE_FORBIDDEN_NAME,
                        value = ORDER_STATUS_CHANGE_FORBIDDEN_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_BAD_REQUEST,
          description = BAD_REQUEST_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = ORDER_CANCEL_SEPARATE_API_NAME,
                        value = ORDER_CANCEL_SEPARATE_API_VALUE),
                    @ExampleObject(
                        name = ORDER_PAID_SEPARATE_NAME,
                        value = ORDER_PAID_SEPARATE_VALUE),
                    @ExampleObject(name = ORDER_STATUS_NULL_NAME, value = ORDER_STATUS_NULL_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_CONFLICT,
          description = CONFLICT_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = ORDER_FINALIZED_STATE_CONFLICT_NAME,
                        value = ORDER_FINALIZED_STATE_CONFLICT_VALUE),
                    @ExampleObject(
                        name = ORDER_STATUS_REGRESSION_NAME,
                        value = ORDER_STATUS_REGRESSION_VALUE),
                    @ExampleObject(
                        name = OFFLINE_ORDER_DELIVERY_STATUS_NAME,
                        value = OFFLINE_ORDER_DELIVERY_STATUS_VALUE)
                  }))
    })
public @interface UpdateOrderStatusOperation {}
