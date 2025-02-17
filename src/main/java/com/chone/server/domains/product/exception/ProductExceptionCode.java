package com.chone.server.domains.product.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.chone.server.commons.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductExceptionCode implements ExceptionCode {
  PRODUCT_NOT_FOUND(NOT_FOUND, "존재하지 않는 상품입니다.");

  private final HttpStatus status;
  private final String message;
  private final String code = this.name();
}