package com.chone.server.domains.review.exception;

import static org.springframework.http.HttpStatus.*;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ReviewExceptionCode implements ExceptionCode {

  // 400 BAD_REQUEST
  INVALID_PARAMETER(BAD_REQUEST, "요청 파라미터가 유효하지 않습니다."),
  INVALID_CONTENT(BAD_REQUEST, "리뷰 내용은 5자 이상이어야 합니다."),

  // 401 UNAUTHORIZED
  REVIEW_UNAUTHORIZED(UNAUTHORIZED, "인증이 필요한 요청입니다."),

  // 403 FORBIDDEN
  REVIEW_FORBIDDEN(FORBIDDEN, "주문한 고객만 리뷰를 작성할 수 있습니다."),
  REVIEW_FORBIDDEN_ACTION(FORBIDDEN, "리뷰 작성자가 아닌 사용자는 해당 작업을 수행할 수 없습니다."),
  REVIEW_UPDATE_EXPIRED(FORBIDDEN, "리뷰는 작성 후 3일 이내에만 수정할 수 있습니다."),

  // 404 NOT_FOUND
  REVIEW_NOT_FOUND(NOT_FOUND, "요청하신 리뷰 정보를 찾을 수 없습니다."),
  ORDER_NOT_FOUND(NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
  STORE_NOT_FOUND(NOT_FOUND, "해당 가게를 찾을 수 없습니다."),

  // 409 CONFLICT
  REVIEW_ALREADY_EXISTS(CONFLICT, "해당 주문에 대한 리뷰가 이미 존재합니다."),

  // 422 UNPROCESSABLE_ENTITY
  INVALID_RATING(UNPROCESSABLE_ENTITY, "평점 값은 1에서 5 사이의 숫자여야 합니다."),
  ORDER_NOT_COMPLETED(UNPROCESSABLE_ENTITY, "완료된 주문에 대해서만 리뷰를 작성할 수 있습니다."),

  // 500 INTERNAL_SERVER_ERROR
  FILE_UPLOAD_ERROR(INTERNAL_SERVER_ERROR, "이미지 업로드 중 오류가 발생했습니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
