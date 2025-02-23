package com.chone.server.domains.payment.document.constants;

public final class PaymentOperationResponseConstants {
  public static final String UNAUTHORIZED_NAME = "인증이 필요함";
  public static final String PAYMENT_NOT_FOUND_NAME = "해당 결제을 찾을 수 없음";
  public static final String CUSTOMER_ACCESS_DENIED_NAME = "접근 권한이 없습니다. 해당 주문의 소유자가 아닙니다.";
  public static final String STORE_OWNER_ACCESS_DENIED_NAME = "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.";

  private PaymentOperationResponseConstants() {}

  public final class List {
    // == 200 OK 응답 ==
    public static final String SUCCESS_DESCRIPTION = "결제 조회 성공";
    public static final String SUCCESS_EXAMPLE =
        """
              {
                  "content": [
                      {
                          "id": "123e4567-e89b-12d3-a456-426614174000",
                          "orderId": "123e4567-e89b-12d3-a456-426614174001",
                          "totalPrice": 15000,
                          "method": "신용카드",
                          "status": "결제완료",
                          "createdAt": "2024-02-12T15:30:00Z",
                          "updatedAt": "2024-02-12T15:30:00Z"
                      }
                  ],
                  "pageInfo": {
                      "page": 0,
                      "size": 10,
                      "totalElements": 50,
                      "totalPages": 5
                  }
              }
          """;

    // == name ==
    public static final String CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED_NAME = "사용자별 주문 조회 권한이 없음";
    public static final String STORE_PAYMENT_FILTERING_ACCESS_DENIED_NAME = "가게별 주문 조회 권한이 없음";

    // == value ==
    public static final String UNAUTHORIZED_VALUE =
        """
                                            {
                                              "httpMethod": "PATCH",
                                              "httpStatus": "401",
                                              "errorCode": "UNAUTHORIZED",
                                              "timestamp": "2025-02-23T15:32:45.123456",
                                              "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                                              "path": "/api/v1/orders/{id}"
                                            }
                                        """;
    public static final String CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED_VALUE =
        """
                  {
                      "httpMethod": "GET",
                      "httpStatus": "403",
                      "errorCode": "CUSTOMER_PAYMENT_FILTERING_ACCESS_DENIED",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "사용자별 주문 조회 권한이 없습니다.",
                      "path": "/api/v1/payments"
                  }
                  """;

    public static final String STORE_PAYMENT_FILTERING_ACCESS_DENIED_VALUE =
        """
                  {
                      "httpMethod": "GET",
                      "httpStatus": "403",
                      "errorCode": "STORE_PAYMENT_FILTERING_ACCESS_DENIED",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "가게별 주문 조회 권한이 없습니다.",
                      "path": "/api/v1/payments"
                  }
                  """;

    private List() {}
  }

  public final class Detail {
    // == 200 OK 응답 ==
    public static final String SUCCESS_DESCRIPTION = "결제 상세 조회 성공";
    public static final String SUCCESS_EXAMPLE =
        """
                  {
                    "payment": {
                      "id": "123e4567-e89b-12d3-a456-426614174000",
                      "status": "COMPLETED",
                      "cancelReason": null,
                      "method": "카드"
                    },
                    "order": {
                      "id": "123e4567-e89b-12d3-a456-426614174001",
                      "totalPrice": 15000,
                      "type": "포장",
                      "userId": 12345,
                      "storeId": "123e4567-e89b-12d3-a456-426614174002",
                      "storeName": "맛있는 식당"
                    }
                  }
              """;

    // == name ==
    public static final String NOT_CUSTOMER_PAYMENT_HISTORY_NAME = "주문자만 결제 내역을 조회할 수 있습니다.";
    public static final String NOT_OWNER_PAYMENT_HISTORY_NAME = "해당 가게의 주인만 결제 내역을 조회할 수 있습니다.";

    // == value ==
    public static final String UNAUTHORIZED_VALUE =
        """
                                        {
                                          "httpMethod": "PATCH",
                                          "httpStatus": "401",
                                          "errorCode": "UNAUTHORIZED",
                                          "timestamp": "2025-02-23T15:32:45.123456",
                                          "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                                          "path": "/api/v1/orders/{id}"
                                        }
                                    """;
    public static final String NOT_FOUND_PAYMENT_VALUE =
        """
                  {
                      "httpMethod": "GET",
                      "httpStatus": "404",
                      "errorCode": "NOT_FOUND_PAYMENT",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "해당 결제 내역을 찾을 수 없습니다.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    public static final String NOT_CUSTOMER_PAYMENT_HISTORY_VALUE =
        """
                  {
                      "httpMethod": "GET",
                      "httpStatus": "403",
                      "errorCode": "NOT_CUSTOMER_PAYMENT_HISTORY",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "주문자만 결제 내역을 조회할 수 있습니다.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;
    public static final String NOT_OWNER_PAYMENT_HISTORY_VALUE =
        """
                  {
                      "httpMethod": "GET",
                      "httpStatus": "403",
                      "errorCode": "NOT_OWNER_PAYMENT_HISTORY",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "해당 가게의 주인만 결제 내역을 조회할 수 있습니다.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    private Detail() {}
  }

  public final class Cancel {
    // == 200 OK 응답 ==
    public static final String SUCCESS_DESCRIPTION = "결제 취소 성공";
    public static final String SUCCESS_EXAMPLE =
        """
      {
        "isSuccess": true,
        "status": "결제 취소됨",
        "cancelRequestedAt": "2024-02-21T15:30:00Z"
      }
      """;

    // == name ==
    public static final String CANCEL_PERMISSION_DENIED_NAME = "결제 취소 권한이 없습니다.";
    public static final String PAYMENT_ALREADY_CANCELED_NAME = "이미 취소된 결제입니다.";
    public static final String FAILED_PAYMENT_NAME = "실패된 결제입니다(결제된 적이 없습니다).";
    public static final String ORDER_NOT_CANCELABLE_NAME = "주문이 취소될 수 없어 결제를 취소할 수 없습니다.";
    public static final String PAYMENT_CANCELLATION_IN_PROGRESS_NAME =
        "결제 취소가 진행 중입니다. 잠시 후 다시 시도해주세요.";
    public static final String PAYMENT_CANCELLATION_ERROR_NAME = "결제 취소 요청이 거부되었습니다.";

    // == value ==
    public static final String UNAUTHORIZED_VALUE =
        """
                                            {
                                              "httpMethod": "PATCH",
                                              "httpStatus": "401",
                                              "errorCode": "UNAUTHORIZED",
                                              "timestamp": "2025-02-23T15:32:45.123456",
                                              "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                                              "path": "/api/v1/orders/{id}"
                                            }
                                        """;
    public static final String NOT_FOUND_PAYMENT_VALUE =
        """
                  {
                      "httpMethod": "PATCH",
                      "httpStatus": "404",
                      "errorCode": "NOT_FOUND_PAYMENT",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "해당 결제 내역을 찾을 수 없습니다.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    public static final String ORDER_CUSTOMER_ACCESS_DENIED_VALUE =
        """
                  {
                      "httpMethod": "PATCH",
                      "httpStatus": "403",
                      "errorCode": "ORDER_CUSTOMER_ACCESS_DENIED",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "접근 권한이 없습니다. 해당 주문의 소유자가 아닙니다.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    public static final String ORDER_STORE_OWNER_ACCESS_DENIED_VALUE =
        """
                  {
                      "httpMethod": "PATCH",
                      "httpStatus": "403",
                      "errorCode": "ORDER_STORE_OWNER_ACCESS_DENIED",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    public static final String CANCEL_PERMISSION_DENIED_VALUE =
        """
                  {
                      "httpMethod": "PATCH",
                      "httpStatus": "403",
                      "errorCode": "CANCEL_PERMISSION_DENIED",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "결제 취소 권한이 없습니다.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    public static final String PAYMENT_ALREADY_CANCELED_VALUE =
        """
                  {
                      "httpMethod": "PATCH",
                      "httpStatus": "409",
                      "errorCode": "PAYMENT_ALREADY_CANCELED",
                      "timestamp": "2025-02-23
                  "httpStatus": "409",
                  "errorCode": "PAYMENT_ALREADY_CANCELED",
                  "timestamp": "2025-02-23T15:32:45.123456",
                  "message": "이미 취소된 결제입니다.",
                  "path": "/api/v1/payments/{id}"
              }
              """;

    public static final String FAILED_PAYMENT_VALUE =
        """
                  {
                      "httpMethod": "PATCH",
                      "httpStatus": "400",
                      "errorCode": "FAILED_PAYMENT",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "실패된 결제입니다(결제된 적이 없습니다).",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    public static final String ORDER_NOT_CANCELABLE_VALUE =
        """
                  {
                      "httpMethod": "PATCH",
                      "httpStatus": "400",
                      "errorCode": "ORDER_NOT_CANCELABLE",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "주문이 취소될 수 없어 결제를 취소할 수 없습니다.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    public static final String PAYMENT_CANCELLATION_IN_PROGRESS_VALUE =
        """
                  {
                      "httpMethod": "PATCH",
                      "httpStatus": "503",
                      "errorCode": "PAYMENT_CANCELLATION_IN_PROGRESS",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "결제 취소가 진행 중입니다. 잠시 후 다시 시도해주세요.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    public static final String PAYMENT_CANCELLATION_ERROR_VALUE =
        """
                  {
                      "httpMethod": "PATCH",
                      "httpStatus": "500",
                      "errorCode": "PAYMENT_CANCELLATION_ERROR",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "결제 취소 처리 중 오류가 발생했습니다.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    public static final String PAYMENT_CANCELLATION_FAILED_VALUE =
        """
                  {
                      "httpMethod": "PATCH",
                      "httpStatus": "503",
                      "errorCode": "PAYMENT_CANCELLATION_FAILED",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "결제 취소 요청이 거부되었습니다.",
                      "path": "/api/v1/payments/{id}"
                  }
                  """;

    private Cancel() {}
  }

  public final class Create {
    // == 200 OK 응답 ==
    public static final String SUCCESS_DESCRIPTION = "결제 생성 성공";
    public static final String SUCCESS_EXAMPLE =
        """
                  {
                    "id": "123e4567-e89b-12d3-a456-426614174000",
                    "orderId": "123e4567-e89b-12d3-a456-426614174000",
                    "totalPrice": 15000,
                    "status": "COMPLETED",
                    "paymentMethod": "CARD",
                    "createdAt": "2024-02-12T15:30:00"
                  }
                  """;

    // == name ==
    public static final String NOT_FOUND_ORDER_NAME = "해당 주문을 찾을 수 없습니다.";
    public static final String ALREADY_PAID_NAME = "이미 결제가 진행된 주문입니다.";
    public static final String CANCELED_ORDER_NAME = "취소된 주문은 결제할 수 없습니다.";
    public static final String PRICE_MISMATCH_NAME = "결제 금액이 주문 금액과 일치하지 않습니다.";
    public static final String NOT_ALLOW_PAY_ONLINE_ORDER_NAME = "고객은 현장 결제에 대한 결제 권한이 없습니다.";
    public static final String NOT_CUSTOMER_PAYMENT_NAME = "주문자만 결제할 수 있습니다.";

    // == value ==
    public static final String UNAUTHORIZED_VALUE =
        """
                  {
                      "httpMethod": "POST",
                      "httpStatus": "401",
                      "errorCode": "UNAUTHORIZED",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                      "path": "/api/v1/payments"
                  }
                  """;

    public static final String NOT_FOUND_ORDER_VALUE =
        """
                  {
                      "httpMethod": "POST",
                      "httpStatus": "404",
                      "errorCode": "NOT_FOUND_ORDER",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "해당 주문을 찾을 수 없습니다.",
                      "path": "/api/v1/payments"
                  }
                  """;

    public static final String ALREADY_PAID_VALUE =
        """
                  {
                      "httpMethod": "POST",
                      "httpStatus": "409",
                      "errorCode": "ALREADY_PAID",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "이미 결제가 진행된 주문입니다.",
                      "path": "/api/v1/payments"
                  }
                  """;

    public static final String CANCELED_ORDER_VALUE =
        """
                  {
                      "httpMethod": "POST",
                      "httpStatus": "400",
                      "errorCode": "CANCELED_ORDER",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "취소된 주문은 결제할 수 없습니다.",
                      "path": "/api/v1/payments"
                  }
                  """;

    public static final String PRICE_MISMATCH_VALUE =
        """
                  {
                      "httpMethod": "POST",
                      "httpStatus": "400",
                      "errorCode": "PRICE_MISMATCH",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "결제 금액이 주문 금액과 일치하지 않습니다.",
                      "path": "/api/v1/payments"
                  }
                  """;

    public static final String NOT_ALLOW_PAY_ONLINE_ORDER_VALUE =
        """
                  {
                      "httpMethod": "POST",
                      "httpStatus": "403",
                      "errorCode": "NOT_ALLOW_PAY_ONLINE_ORDER",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "고객은 현장 결제에 대한 결제 권한이 없습니다.",
                      "path": "/api/v1/payments"
                  }
                  """;

    public static final String NOT_CUSTOMER_PAYMENT_VALUE =
        """
                  {
                      "httpMethod": "POST",
                      "httpStatus": "403",
                      "errorCode": "NOT_CUSTOMER_PAYMENT",
                      "timestamp": "2025-02-23T15:32:45.123456",
                      "message": "주문자만 결제할 수 있습니다.",
                      "path": "/api/v1/payments"
                  }
                  """;

    private Create() {}
  }
}
