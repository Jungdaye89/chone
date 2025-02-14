package com.chone.server.commons.exception.dto;

import java.time.LocalDateTime;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class ExceptionResponse {
  private String httpMethod;
  private int httpStatus;
  private String errorCode;
  @Builder.Default private String timestamp = LocalDateTime.now().toString();
  private String message;
  private Map<String, String> details;
  private String path;
}
