package com.chone.server.domains.payment.document;

import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_FORBIDDEN;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_NOT_FOUND;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_OK;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_UNAUTHORIZED;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.FORBIDDEN_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.MEDIA_TYPE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.Payment_NOT_FOUND_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.SECURITY_REQUIREMENT;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.UNAUTHORIZED_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationDescriptionConstants.DETAIL_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationDescriptionConstants.DETAIL_SUMMARY;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Detail.NOT_CUSTOMER_PAYMENT_HISTORY_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Detail.NOT_CUSTOMER_PAYMENT_HISTORY_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Detail.NOT_FOUND_PAYMENT_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Detail.NOT_OWNER_PAYMENT_HISTORY_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Detail.NOT_OWNER_PAYMENT_HISTORY_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Detail.SUCCESS_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Detail.SUCCESS_EXAMPLE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.Detail.UNAUTHORIZED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.PAYMENT_NOT_FOUND_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.UNAUTHORIZED_NAME;

import com.chone.server.domains.payment.dto.response.PaymentDetailResponse;
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
                  schema = @Schema(implementation = PaymentDetailResponse.class),
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
          description = Payment_NOT_FOUND_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(name = PAYMENT_NOT_FOUND_NAME, value = NOT_FOUND_PAYMENT_VALUE)
                  })),
      @ApiResponse(
          responseCode = CODE_FORBIDDEN,
          description = FORBIDDEN_DESCRIPTION,
          content =
              @Content(
                  mediaType = MEDIA_TYPE,
                  examples = {
                    @ExampleObject(
                        name = NOT_CUSTOMER_PAYMENT_HISTORY_NAME,
                        value = NOT_CUSTOMER_PAYMENT_HISTORY_VALUE),
                    @ExampleObject(
                        name = NOT_OWNER_PAYMENT_HISTORY_NAME,
                        value = NOT_OWNER_PAYMENT_HISTORY_VALUE)
                  }))
    })
public @interface PaymentDetailOperation {}
