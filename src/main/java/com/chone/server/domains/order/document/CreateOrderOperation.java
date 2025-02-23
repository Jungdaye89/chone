package com.chone.server.domains.order.document;

import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.BAD_REQUEST_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_BAD_REQUEST;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_CREATED;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_FORBIDDEN;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_UNAUTHORIZED;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.FORBIDDEN_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.MEDIA_TYPE;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.SECURITY_REQUIREMENT;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.CREATE_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.CREATE_SUMMARY;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.MISSING_DELIVERY_ADDRESS_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.MISSING_DELIVERY_ADDRESS_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.MISSING_ORDER_ITEMS_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.MISSING_ORDER_ITEMS_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.MISSING_STORE_ID_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.MISSING_STORE_ID_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.MULTIPLE_STORE_ORDER_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.MULTIPLE_STORE_ORDER_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.ORDER_STORE_OWNER_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.STORE_NOT_FOUND_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.STORE_NOT_FOUND_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.SUCCESS_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.SUCCESS_EXAMPLE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Create.UNAUTHORIZED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.ORDER_STORE_OWNER_ACCESS_DENIED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UNAUTHORIZED_NAME;

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
@Operation(summary = CREATE_SUMMARY, description = CREATE_DESCRIPTION)
@SecurityRequirement(name = SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = CODE_CREATED,
          description = SUCCESS_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  schema = @Schema(implementation = CreateOrderResponse.class),
                  examples = @ExampleObject(value = SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = CODE_UNAUTHORIZED,
          description = BAD_REQUEST_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(name = UNAUTHORIZED_NAME, value = UNAUTHORIZED_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_BAD_REQUEST,
          description = BAD_REQUEST_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(name = MISSING_STORE_ID_NAME, value = MISSING_STORE_ID_VALUE),
                    @ExampleObject(
                        name = MISSING_ORDER_ITEMS_NAME,
                        value = MISSING_ORDER_ITEMS_VALUE),
                    @ExampleObject(name = STORE_NOT_FOUND_NAME, value = STORE_NOT_FOUND_VALUE),
                    @ExampleObject(
                        name = MULTIPLE_STORE_ORDER_NAME,
                        value = MULTIPLE_STORE_ORDER_VALUE),
                    @ExampleObject(
                        name = MISSING_DELIVERY_ADDRESS_NAME,
                        value = MISSING_DELIVERY_ADDRESS_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_FORBIDDEN,
          description = FORBIDDEN_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = ORDER_STORE_OWNER_ACCESS_DENIED_NAME,
                        value = ORDER_STORE_OWNER_ACCESS_DENIED_VALUE)
                  }))
    })
public @interface CreateOrderOperation {}
