package com.chone.server.commons.handler;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.exception.ExceptionCode;
import com.chone.server.commons.exception.GlobalExceptionCode;
import com.chone.server.commons.exception.dto.ExceptionResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Log4j2(topic = "Global Exception")
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(ApiBusinessException.class)
  public final ResponseEntity<ExceptionResponse> handleApiBusinessException(
      ApiBusinessException exc, HttpServletRequest request) {
    ExceptionCode exceptionCode = exc.getExceptionCode();

    ExceptionResponse response = createErrorResponse(request, exceptionCode, null);

    logError(exc, exceptionCode);

    return ResponseEntity.status(exceptionCode.getStatus()).body(response);
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<ExceptionResponse> handleRuntimeException(
      RuntimeException exc, HttpServletRequest request) {
    ExceptionCode exceptionCode = GlobalExceptionCode.INTERNAL_ERROR;

    ExceptionResponse response = createErrorResponse(request, exceptionCode, null);

    logError(exc, exceptionCode);

    return ResponseEntity.status(exceptionCode.getStatus()).body(response);
  }

  private ExceptionResponse createErrorResponse(
      HttpServletRequest request, ExceptionCode exceptionCode, Map<String, String> details) {
    return ExceptionResponse.builder()
        .httpMethod(request.getMethod())
        .httpStatus(exceptionCode.getStatus().value())
        .errorCode(exceptionCode.getCode())
        .message(exceptionCode.getMessage())
        .path(request.getRequestURI())
        .details(details)
        .build();
  }

  private void logError(Exception exception, ExceptionCode exceptionCode) {
    log.error(
        "Exception occurred - Message: {}, Code: {}",
        exception.getMessage(),
        exceptionCode.getCode());
  }
}
