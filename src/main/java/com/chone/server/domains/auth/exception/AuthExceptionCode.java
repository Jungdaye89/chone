package com.chone.server.domains.auth.exception;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {
  // 인증
  INVALID_TOKEN(HttpStatus.FORBIDDEN, "유효하지 않은 JwtToken"),
  EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "만료된 JwtToken");

  //상태, 메세지, 에러코드
  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
