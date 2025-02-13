package com.chone.server.commons.exception;

import org.springframework.http.HttpStatusCode;

public interface ExceptionCode {
  HttpStatusCode getStatus();

  String getMessage();

  String getCode();
}
