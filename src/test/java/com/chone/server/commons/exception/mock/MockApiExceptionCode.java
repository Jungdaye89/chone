package com.chone.server.commons.exception.mock;

import com.chone.server.commons.exception.ExceptionCode;
import org.springframework.http.HttpStatus;

public enum MockApiExceptionCode implements ExceptionCode {
  NOT_FOUND(HttpStatus.NOT_FOUND, "MOCK-404", "Mock Resource Not Found"),
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "MOCK-400", "Mock Bad Request"),
  INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "MOCK-500", "Mock Internal Error");

  private final HttpStatus status;
  private final String code;
  private final String message;

  MockApiExceptionCode(HttpStatus status, String code, String message) {
    this.status = status;
    this.code = code;
    this.message = message;
  }

  @Override
  public HttpStatus getStatus() {
    return status;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public String getMessage() {
    return message;
  }
}
