package com.chone.server.domains.payment.document;

import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.BAD_REQUEST_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_BAD_REQUEST;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_CONFLICT;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_FORBIDDEN;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_INTERNAL_SERVER_ERROR;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_NOT_FOUND;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_OK;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_SERVICE_UNAVAILABLE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_UNAUTHORIZED;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CONFLICT_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.FORBIDDEN_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.INTERNAL_SERVER_ERROR_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.MEDIA_TYPE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.NOT_FOUND_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.SECURITY_REQUIREMENT;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.SERVICE_UNAVAILABLE_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.UNAUTHORIZED_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationDescriptionConstants.CANCEL_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationDescriptionConstants.CANCEL_SUMMARY;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.CUSTOMER_ACCESS_DENIED_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.CANCEL_PERMISSION_DENIED_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.CANCEL_PERMISSION_DENIED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.FAILED_PAYMENT_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.FAILED_PAYMENT_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.NOT_FOUND_PAYMENT_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.ORDER_CUSTOMER_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.ORDER_NOT_CANCELABLE_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.ORDER_NOT_CANCELABLE_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.ORDER_STORE_OWNER_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.PAYMENT_ALREADY_CANCELED_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.PAYMENT_ALREADY_CANCELED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.PAYMENT_CANCELLATION_ERROR_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.PAYMENT_CANCELLATION_ERROR_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.PAYMENT_CANCELLATION_FAILED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.PAYMENT_CANCELLATION_IN_PROGRESS_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.PAYMENT_CANCELLATION_IN_PROGRESS_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.SUCCESS_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.SUCCESS_EXAMPLE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Cancel.UNAUTHORIZED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.PAYMENT_NOT_FOUND_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.STORE_OWNER_ACCESS_DENIED_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.UNAUTHORIZED_NAME;

import com.chone.server.domains.payment.dto.response.CancelPaymentResponse;
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
                  schema = @Schema(implementation = CancelPaymentResponse.class),
                  examples = @ExampleObject(value = SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = CODE_NOT_FOUND,
          description = NOT_FOUND_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(name = PAYMENT_NOT_FOUND_NAME, value = NOT_FOUND_PAYMENT_VALUE)
                  })),
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
                        name = CUSTOMER_ACCESS_DENIED_NAME,
                        value = ORDER_CUSTOMER_ACCESS_DENIED_VALUE),
                    @ExampleObject(
                        name = STORE_OWNER_ACCESS_DENIED_NAME,
                        value = ORDER_STORE_OWNER_ACCESS_DENIED_VALUE),
                    @ExampleObject(
                        name = CANCEL_PERMISSION_DENIED_NAME,
                        value = CANCEL_PERMISSION_DENIED_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_CONFLICT,
          description = CONFLICT_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = PAYMENT_ALREADY_CANCELED_NAME,
                        value = PAYMENT_ALREADY_CANCELED_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_BAD_REQUEST,
          description = BAD_REQUEST_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(name = FAILED_PAYMENT_NAME, value = FAILED_PAYMENT_VALUE),
                    @ExampleObject(
                        name = ORDER_NOT_CANCELABLE_NAME,
                        value = ORDER_NOT_CANCELABLE_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_SERVICE_UNAVAILABLE,
          description = SERVICE_UNAVAILABLE_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = PAYMENT_CANCELLATION_IN_PROGRESS_NAME,
                        value = PAYMENT_CANCELLATION_IN_PROGRESS_VALUE),
                    @ExampleObject(
                        name = PAYMENT_CANCELLATION_ERROR_NAME,
                        value = PAYMENT_CANCELLATION_ERROR_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_INTERNAL_SERVER_ERROR,
          description = INTERNAL_SERVER_ERROR_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = @ExampleObject(value = PAYMENT_CANCELLATION_FAILED_VALUE)))
    })
public @interface CancelPaymentOperation {}
