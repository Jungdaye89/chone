package com.chone.server.domains.review.exception;

import static org.springframework.http.HttpStatus.*;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewExceptionCode implements ExceptionCode {
  INVALID_RATING(BAD_REQUEST, "평점은 1-5 사이의 값이어야 합니다."),
  INVALID_CONTENT(BAD_REQUEST, "리뷰 내용은 5자 이상이어야 합니다."),
  REVIEW_UNAUTHORIZED(UNAUTHORIZED, "인증이 필요한 요청입니다."),
  REVIEW_FORBIDDEN(FORBIDDEN, "주문한 고객만 리뷰를 작성할 수 있습니다."),
  ORDER_NOT_FOUND(NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
  STORE_NOT_FOUND(NOT_FOUND, "해당 가게를 찾을 수 없습니다."),
  REVIEW_ALREADY_EXISTS(CONFLICT, "해당 주문에 대한 리뷰가 이미 존재합니다."),
  ORDER_NOT_COMPLETED(UNPROCESSABLE_ENTITY, "완료된 주문에 대해서만 리뷰를 작성할 수 있습니다."),
  FILE_UPLOAD_ERROR(INTERNAL_SERVER_ERROR, "이미지 업로드 중 오류가 발생했습니다."),
  REVIEW_ACCESS_DENIED(FORBIDDEN, "해당 요청을 수행할 권한이 없습니다."),
  REVIEW_NOT_FOUND(NOT_FOUND, "요청하신 리뷰 정보를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
