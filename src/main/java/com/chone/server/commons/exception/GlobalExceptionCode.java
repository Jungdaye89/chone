package com.chone.server.commons.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalExceptionCode implements ExceptionCode {
  USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 사용자입니다."),
  CATEGORY_NOT_FOUND(NOT_FOUND, "존재하지 않는 카테고리입니다."),
  LEGAL_DONG_NOT_FOUND(NOT_FOUND, "존재하지 않는 법정동입니다."),
  STORE_NOT_FOUND(NOT_FOUND, "존재하지 않는 가게입니다."),
  USER_OWNED_STORE_NOT_FOUND(NOT_FOUND, "사용자가 소유한 가게가 없습니다."),
  INTERNAL_ERROR(INTERNAL_SERVER_ERROR, "서버 내부 오류로 인해 요청을 처리할 수 없습니다."),
  INVALID_INPUT(BAD_REQUEST, "입력하신 데이터에 오류가 있습니다. 요청 내용을 확인하고 다시 시도해 주세요."),
  NOT_READABLE(BAD_REQUEST, "입력하신 데이터가 잘못된 형식입니다."),
  INVALID_REQUEST(BAD_REQUEST, "요청하신 값이 올바르지 않습니다. 요청 값을 확인해 주세요.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}