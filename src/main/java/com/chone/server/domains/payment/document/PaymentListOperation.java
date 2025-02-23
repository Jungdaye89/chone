package com.chone.server.domains.payment.document;

import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_FORBIDDEN;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_OK;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.CODE_UNAUTHORIZED;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.FORBIDDEN_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.MEDIA_TYPE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.SECURITY_REQUIREMENT;
import static com.chone.server.domains.payment.document.constants.PaymentOperationCommonConstants.UNAUTHORIZED_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationDescriptionConstants.LIST_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationDescriptionConstants.LIST_SUMMARY;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.List.CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.List.CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.List.STORE_PAYMENT_FILTERING_ACCESS_DENIED_NAME;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.List.STORE_PAYMENT_FILTERING_ACCESS_DENIED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.List.SUCCESS_DESCRIPTION;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.List.SUCCESS_EXAMPLE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.List.UNAUTHORIZED_VALUE;
import static com.chone.server.domains.payment.document.constants.PaymentOperationResponseConstants.UNAUTHORIZED_NAME;

import com.chone.server.domains.payment.dto.request.PaymentFilterParams;
import com.chone.server.domains.payment.dto.response.PageResponse;
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
          in = ParameterIn.QUERY,
          description = "결제 필터링 파라미터",
          schema = @Schema(implementation = PaymentFilterParams.class)),
      @Parameter(
          name = "page",
          in = ParameterIn.QUERY,
          description = "페이지 번호 (0부터 시작)",
          schema = @Schema(type = "integer", defaultValue = "0")),
      @Parameter(
          name = "size",
          in = ParameterIn.QUERY,
          description = "페이지 크기",
          schema = @Schema(type = "integer", defaultValue = "10")),
      @Parameter(
          name = "sort",
          in = ParameterIn.QUERY,
          description = "정렬 기준",
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
                        name = CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED_NAME,
                        value = CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED_VALUE),
                    @ExampleObject(
                        name = STORE_PAYMENT_FILTERING_ACCESS_DENIED_NAME,
                        value = STORE_PAYMENT_FILTERING_ACCESS_DENIED_VALUE)
                  }))
    })
public @interface PaymentListOperation {}
