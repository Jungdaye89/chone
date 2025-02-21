package com.chone.server.domains.delivery.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum DeliveryExceptionCode implements ExceptionCode {
  NOT_FOUND_DELIVERY(NOT_FOUND, "해당 배달 이력을 찾을 수 없습니다."),
  ;
  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}
