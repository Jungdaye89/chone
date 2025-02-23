package com.chone.server.domains.payment.document;

import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.BAD_REQUEST_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_BAD_REQUEST;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_CONFLICT;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_CREATED;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_FORBIDDEN;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_NOT_FOUND;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_UNAUTHORIZED;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CONFLICT_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.FORBIDDEN_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.MEDIA_TYPE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.ORDER_NOT_FOUND_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.SECURITY_REQUIREMENT;
import static com.chone.server.domains.payment.document.constants.PaymentOperationDescriptionConstants.CREATE_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationDescriptionConstants.CREATE_SUMMARY;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.ALREADY_PAID_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.ALREADY_PAID_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.CANCELED_ORDER_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.CANCELED_ORDER_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.NOT_ALLOW_PAY_ONLINE_ORDER_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.NOT_ALLOW_PAY_ONLINE_ORDER_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.NOT_CUSTOMER_PAYMENT_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.NOT_CUSTOMER_PAYMENT_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.NOT_FOUND_ORDER_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.NOT_FOUND_ORDER_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.PRICE_MISMATCH_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.PRICE_MISMATCH_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.SUCCESS_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.SUCCESS_EXAMPLE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Create.UNAUTHORIZED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.UNAUTHORIZED_NAME;

import com.chone.server.domains.payment.dto.response.CreatePaymentResponse;
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
                  schema = @Schema(implementation = CreatePaymentResponse.class),
                  examples = @ExampleObject(value = SUCCESS_EXAMPLE))),
      @ApiResponse(
          responseCode = CODE_UNAUTHORIZED,
          description = "인증이 필요함",
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
                    @ExampleObject(name = NOT_FOUND_ORDER_NAME, value = NOT_FOUND_ORDER_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_CONFLICT,
          description = CONFLICT_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(name = ALREADY_PAID_NAME, value = ALREADY_PAID_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_BAD_REQUEST,
          description = BAD_REQUEST_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(name = CANCELED_ORDER_NAME, value = CANCELED_ORDER_VALUE),
                    @ExampleObject(name = PRICE_MISMATCH_NAME, value = PRICE_MISMATCH_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_FORBIDDEN,
          description = FORBIDDEN_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = NOT_ALLOW_PAY_ONLINE_ORDER_NAME,
                        value = NOT_ALLOW_PAY_ONLINE_ORDER_VALUE),
                    @ExampleObject(
                        name = NOT_CUSTOMER_PAYMENT_NAME,
                        value = NOT_CUSTOMER_PAYMENT_VALUE)
                  }))
    })
public @interface CreatePaymentOperation {}
