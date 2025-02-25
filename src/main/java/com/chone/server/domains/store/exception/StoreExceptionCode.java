package com.chone.server.domains.store.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StoreExceptionCode implements ExceptionCode {
  USER_NOT_FOUND(NOT_FOUND, "존재하지 않는 사용자입니다."),
  USER_NOT_OWNER(BAD_REQUEST, "해당 사용자는 가게를 소유할 수 없습니다."),
  CATEGORY_NOT_FOUND(NOT_FOUND, "존재하지 않는 카테고리입니다."),
  LEGAL_DONG_NOT_FOUND(NOT_FOUND, "존재하지 않는 법정동입니다."),
  STORE_NOT_FOUND(NOT_FOUND, "존재하지 않는 가게입니다."),
  USER_OWNED_STORE_NOT_FOUND(NOT_FOUND, "사용자가 소유한 가게가 없습니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}