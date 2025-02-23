package com.chone.server.domains.order.document;

import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_FORBIDDEN;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_NOT_FOUND;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_OK;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.CODE_UNAUTHORIZED;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.MEDIA_TYPE;
import static com.chone.server.domains.order.document.constants.OrderOperationCommonConstants.SECURITY_REQUIREMENT;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.DETAIL_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationDescriptionConstants.DETAIL_SUMMARY;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Detail.NOT_FOUND_ORDER_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Detail.ORDER_ACCESS_DENIED_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Detail.ORDER_CUSTOMER_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Detail.ORDER_STORE_OWNER_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Detail.SUCCESS_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Detail.SUCCESS_EXAMPLE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.Detail.UNAUTHORIZED_VALUE;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.ORDER_CUSTOMER_ACCESS_DENIED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.ORDER_NOT_FOUND_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.ORDER_NOT_FOUND_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.ORDER_STORE_OWNER_ACCESS_DENIED_NAME;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UNAUTHORIZED_DESCRIPTION;
import static com.chone.server.domains.order.document.constants.OrderOperationResponseConstants.UNAUTHORIZED_NAME;

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
@Operation(summary = DETAIL_SUMMARY, description = DETAIL_DESCRIPTION)
@SecurityRequirement(name = SECURITY_REQUIREMENT)
@ApiResponses(
    value = {
      @ApiResponse(
          responseCode = CODE_OK,
          description = SUCCESS_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  schema = @Schema(implementation = OrderDetailResponse.class),
                  examples = @ExampleObject(value = SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = CODE_FORBIDDEN,
          description = ORDER_ACCESS_DENIED_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = ORDER_CUSTOMER_ACCESS_DENIED_NAME,
                        value = ORDER_CUSTOMER_ACCESS_DENIED_VALUE),
                    @ExampleObject(
                        name = ORDER_STORE_OWNER_ACCESS_DENIED_NAME,
                        value = ORDER_STORE_OWNER_ACCESS_DENIED_VALUE)
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
          responseCode = CODE_UNAUTHORIZED,
          description = UNAUTHORIZED_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(name = UNAUTHORIZED_NAME, value = UNAUTHORIZED_VALUE)
                  }))
    })
public @interface OrderDetailOperation {}
