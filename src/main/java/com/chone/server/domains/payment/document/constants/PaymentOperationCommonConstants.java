package com.chone.server.domains.payment.document.constants;

public final class PaymentOperationCommonConstants {
  // API 공통
  public static final String SECURITY_REQUIREMENT = "Bearer Authentication";
  public static final String MEDIA_TYPE = "application/json";

  // Response Descriptions
  public static final String FORBIDDEN_DESCRIPTION = "접근 권한이 없음";
  public static final String UNAUTHORIZED_DESCRIPTION = "인증이 필요함";
  public static final String Payment_NOT_FOUND_DESCRIPTION = "결제을 찾을 수 없음";
  public static final String BAD_REQUEST_DESCRIPTION = "잘못된 요청";
  public static final String CONFLICT_DESCRIPTION = "요청 충돌";
  public static final String NOT_FOUND_DESCRIPTION = "결제 내역을 찾을 수 없음";
  public static final String ORDER_NOT_FOUND_DESCRIPTION = "주문을 찾을 수 없음";
  public static final String SERVICE_UNAVAILABLE_DESCRIPTION = "서비스가 사용 불가능";
  public static final String INTERNAL_SERVER_ERROR_DESCRIPTION = "서버 오류";

  // Response Codes
  public static final String CODE_OK = "200";
  public static final String CODE_CREATED = "201";
  public static final String CODE_BAD_REQUEST = "400";
  public static final String CODE_UNAUTHORIZED = "401";
  public static final String CODE_FORBIDDEN = "403";
  public static final String CODE_NOT_FOUND = "404";
  public static final String CODE_CONFLICT = "409";
  public static final String CODE_SERVICE_UNAVAILABLE = "503";
  public static final String CODE_INTERNAL_SERVER_ERROR = "500";

  private PaymentOperationCommonConstants() {}
}
