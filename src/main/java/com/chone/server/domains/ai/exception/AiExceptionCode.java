package com.chone.server.domains.ai.exception;

import static org.springframework.http.HttpStatus.*;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AiExceptionCode implements ExceptionCode {
  AI_INVALID_CONTENT(BAD_REQUEST, "content 필드는 비어 있을 수 없습니다."),
  AI_UNAUTHORIZED(UNAUTHORIZED, "인증이 필요한 요청입니다."),
  AI_FORBIDDEN(FORBIDDEN, "AI 설명 생성을 요청할 권한이 없습니다."),
  AI_SERVICE_ERROR(INTERNAL_SERVER_ERROR, "AI 서비스 처리 중 오류가 발생했습니다."),
  AI_SERVICE_UNAVAILABLE(SERVICE_UNAVAILABLE, "현재 AI 서비스가 이용 불가능합니다. 나중에 다시 시도해주세요."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
  STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다."),
  PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 상품을 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
