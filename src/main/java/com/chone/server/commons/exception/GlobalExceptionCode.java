package com.chone.server.commons.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionCode implements ExceptionCode {
  INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "서버 내부 오류로 인해 요청을 처리할 수 없습니다."),
  INVALID_INPUT(BAD_REQUEST, "입력하신 데이터에 오류가 있습니다. 요청 내용을 확인하고 다시 시도해 주세요.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
