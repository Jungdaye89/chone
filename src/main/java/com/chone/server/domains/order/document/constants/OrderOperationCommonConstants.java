package com.chone.server.domains.order.document.constants;

public final class OrderOperationCommonConstants {
  // API 공통
  public static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  public static final String MEDIA_TYPE = "application/json";

  // Response Codes
  public static final String CODE_OK = "200";
  public static final String CODE_CREATED = "201";
  public static final String CODE_BAD_REQUEST = "400";
  public static final String CODE_UNAUTHORIZED = "401";
  public static final String CODE_FORBIDDEN = "403";
  public static final String CODE_NOT_FOUND = "404";
  public static final String CODE_CONFLICT = "409";

  private OrderOperationCommonConstants() {}
}
