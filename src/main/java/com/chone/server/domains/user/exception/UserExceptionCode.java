package com.chone.server.domains.user.exception;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserExceptionCode implements ExceptionCode {
  // 회원
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 사용자입니다."),
  USER_EXIST_USERNAME(HttpStatus.BAD_REQUEST, "이미 사용 중인 아이디입니다."),
  USER_EXIST_EMAIL(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일입니다."),
  USER_NOT_AUTHORITY(HttpStatus.FORBIDDEN, "권한이 없습니다."),
  USER_CANT_LOGIN(HttpStatus.FORBIDDEN, "비활성화된 계정입니다."),
  USER_ALREADY_DEACTIVE(HttpStatus.FORBIDDEN,"이미 비활성화된 계정입니다."),
  USER_ALREADY_ACTIVE(HttpStatus.BAD_REQUEST,"이미 활성화된 계정입니다."),
  USER_DELETED(HttpStatus.FORBIDDEN, "삭제된 계정입니다.");


  // 상태, 메시지, 에러코드
  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
