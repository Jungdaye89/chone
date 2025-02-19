package com.chone.server.domains.s3.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum S3ExceptionCode implements ExceptionCode {
  INVALID_FILE_TYPE(BAD_REQUEST, "이미지 파일이 아닙니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}