package com.chone.server.domains.order.document.constants;

public final class OrderOperationResponseConstants {
  public static final String FORBIDDEN_DESCRIPTION = "접근 권한이 없음";
  public static final String UNAUTHORIZED_DESCRIPTION = "인증이 필요함";
  public static final String ORDER_NOT_FOUND_DESCRIPTION = "주문을 찾을 수 없음";
  public static final String BAD_REQUEST_DESCRIPTION = "잘못된 요청";
  public static final String CONFLICT_DESCRIPTION = "잘못된 요청";

  public static final String UNAUTHORIZED_NAME = "인증이 필요함";
  public static final String ORDER_NOT_FOUND_NAME = "해당 주문을 찾을 수 없음";
  public static final String ORDER_CUSTOMER_ACCESS_DENIED_NAME = "해당 주문의 소유자가 아니라 리소스 접근 권한 없음";
  public static final String ORDER_STORE_OWNER_ACCESS_DENIED_NAME = "해당 매장의 소유자가 아니라 리소스 접근 권한 없음";

  private OrderOperationResponseConstants() {}

  public final class List {
    // == 200 OK 응답 ==
    public static final String SUCCESS_DESCRIPTION = "주문 조회 성공";
    public static final String SUCCESS_EXAMPLE =
        """
      {
          "content": [
              {
                  "id": "3ec24ba4-0164-45d1-9566-73dde029b2c1",
                  "storeId": "db8ad8d5-4b9e-454c-82a0-d5ca1eadc7c1",
                  "storeName": "종로카페",
                  "type": "OFFLINE",
                  "status": "PENDING",
                  "totalPrice": 13000,
                  "createdAt": "2025-02-18T11:52:22.289518",
                  "updatedAt": "2025-02-18T11:52:22.289518"
              }
          ],
          "pageInfo": {
              "page": 0,
              "size": 10,
              "totalElements": 4,
              "totalPages": 1,
              "last": true
          }
      }
      """;

    // == name ==
    public static final String CUSTOMER_ORDER_FILTERING_ACCESS_DENIED_NAME = "사용자별 주문 조회 권한이 없습니다.";
    public static final String STORE_ORDER_FILTERING_ACCESS_DENIED_NAME = "가게별 주문 조회 권한이 없습니다.";

    // == value ==
    public static final String UNAUTHORIZED_VALUE =
        """
                                        {
                                          "httpMethod": "GET",
                                          "httpStatus": "401",
                                          "errorCode": "UNAUTHORIZED",
                                          "timestamp": "2025-02-23T15:32:45.123456",
                                          "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                                          "path": "/api/v1/orders"
                                        }
                                    """;
    public static final String CUSTOMER_ORDER_FILTERING_ACCESS_DENIED_VALUE =
        """
                                        {
                                          "httpMethod": "GET",
                                          "httpStatus": "403",
                                          "errorCode": "CUSTOMER_ORDER_FILTERING_ACCESS_DENIED",
                                          "timestamp": "2025-02-23T15:32:45.123456",
                                          "message": "사용자별 주문 조회 권한이 없습니다.",
                                          "path": "/api/v1/orders"
                                        }
                                    """;
    public static final String STORE_ORDER_FILTERING_ACCESS_DENIED_VALUE =
        """
                                        {
                                          "httpMethod": "GET",
                                          "httpStatus": "403",
                                          "errorCode": "STORE_ORDER_FILTERING_ACCESS_DENIED",
                                          "timestamp": "2025-02-23T15:32:45.123456",
                                          "message": "가게별 주문 조회 권한이 없습니다.",
                                          "path": "/api/v1/orders"
                                        }
                                    """;

    private List() {}
  }

  public final class Detail {
    // == 200 OK 응답 ==
    public static final String SUCCESS_DESCRIPTION = "주문 상세 조회 성공";
    public static final String SUCCESS_EXAMPLE =
        """
                              {
                                "order": {
                                  "id": "3ec24ba4-0164-45d1-9566-73dde029b2c1",
                                  "type": "OFFLINE",
                                  "status": "PENDING",
                                  "totalPrice": 13000,
                                  "cancelReason": null,
                                  "request": "포장해주세요"
                                },
                                "user": {
                                  "id": 1,
                                  "username": "홍길동"
                                },
                                "store": {
                                  "id": "db8ad8d5-4b9e-454c-82a0-d5ca1eadc7c1",
                                  "storeName": "종로카페"
                                },
                                "orderItems": [
                                  {
                                    "id": "5db71f2e-34a5-4232-9670-87654f761abc",
                                    "productName": "아메리카노",
                                    "amount": 2
                                  },
                                  {
                                    "id": "9c8b72d1-45e2-41b3-a58c-98765c432def",
                                    "productName": "치즈케이크",
                                    "amount": 1
                                  }
                                ]
                              }
        """;

    // == name ==
    public static final String ORDER_ACCESS_DENIED_DESCRIPTION = "주문 상세 보기에 권한이 없음";

    // == value ==
    public static final String ORDER_CUSTOMER_ACCESS_DENIED_VALUE =
        """
                            {
                              "httpMethod": "GET",
                              "httpStatus": "403",
                              "errorCode": "ORDER_CUSTOMER_ACCESS_DENIED",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "접근 권한이 없습니다. 해당 주문의 소유자가 아닙니다.",
                              "path": "/api/orders/3ec24ba4-0164-45d1-9566-73dde029b2c1"
                            }
                        """;
    public static final String ORDER_STORE_OWNER_ACCESS_DENIED_VALUE =
        """
                            {
                              "httpMethod": "GET",
                              "httpStatus": "403",
                              "errorCode": "ORDER_STORE_OWNER_ACCESS_DENIED",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.",
                              "path": "/api/orders/3ec24ba4-0164-45d1-9566-73dde029b2c1"
                            }
                        """;
    public static final String NOT_FOUND_ORDER_VALUE =
        """
                            {
                              "httpMethod": "GET",
                              "httpStatus": "404",
                              "errorCode": "NOT_FOUND_ORDER",
                              "timestamp": "2025-02-23T15:32:45.123456",
                              "message": "해당 주문을 찾을 수 없습니다.",
                              "path": "/api/orders/3ec24ba4-0164-45d1-9566-73dde029b2c1"
                            }
                        """;
    public static final String UNAUTHORIZED_VALUE =
        """
                              {
                                "httpMethod": "GET",
                                "httpStatus": "401",
                                "errorCode": "UNAUTHORIZED",
                                "timestamp": "2025-02-23T15:32:45.123456",
                                "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                                "path": "/api/orders/3ec24ba4-0164-45d1-9566-73dde029b2c1"
                              }
                          """;

    private Detail() {}
  }

  public final class Cancel {
    // == 200 OK 응답 ==
    public static final String SUCCESS_DESCRIPTION = "주문 취소 성공";
    public static final String SUCCESS_EXAMPLE =
        """
                                        {
                                          "message": "주문이 성공적으로 취소되었습니다."
                                        }
                                        """;

    // == name ==
    public static final String ORDER_CANCEL_PERMISSION_DENIED_NAME = "주문 취소 권한이 없습니다.";
    public static final String ORDER_ALREADY_CANCELED_NAME = "이미 취소된 주문입니다.";
    public static final String ORDER_NOT_CANCELABLE_NAME = "취소할 수 없는 주문입니다.";
    public static final String ORDER_CANCELLATION_TIMEOUT_NAME = "주문 생성 후 5분이 경과하여 취소할 수 없습니다.";
    public static final String FAILED_PAYMENT_NAME = "실패된 결제입니다(결제된 적이 없습니다).";
    public static final String ORDER_REASON_NULL_NAME = "취소 이유는 필수입니다.";
    public static final String ORDER_ALREADY_COMPLETED_NAME = "완료된 주문은 취소할 수 없습니다.";
    public static final String ORDER_PREPARATION_STARTED_NAME = "음식 준비가 시작된 주문은 취소할 수 없습니다.";
    public static final String PAYMENT_ALREADY_CANCELED_NAME = "이미 취소된 결제입니다.";
    public static final String PAYMENT_CANCELLATION_FAILED_NAME = "결제 취소 요청이 거부되었습니다.";

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
    public static final String NOT_FOUND_ORDER_VALUE =
        """
                                      {
                                        "httpMethod": "PATCH",
                                        "httpStatus": "404",
                                        "errorCode": "NOT_FOUND_ORDER",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "해당 주문을 찾을 수 없습니다.",
                                        "path": "/api/v1/orders/{id}"
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
                                        "path": "/api/v1/orders/{id}"
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
                                        "path": "/api/v1/orders/{id}"
                                      }
                                  """;
    public static final String ORDER_CANCEL_PERMISSION_DENIED_VALUE =
        """
                                      {
                                        "httpMethod": "PATCH",
                                        "httpStatus": "403",
                                        "errorCode": "ORDER_CANCEL_PERMISSION_DENIED",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "주문 취소 권한이 없습니다.",
                                        "path": "/api/v1/orders/{id}"
                                      }
                                  """;
    public static final String ORDER_ALREADY_CANCELED_VALUE =
        """
                                      {
                                        "httpMethod": "PATCH",
                                        "httpStatus": "400",
                                        "errorCode": "ORDER_ALREADY_CANCELED",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "이미 취소된 주문입니다.",
                                        "path": "/api/v1/orders/{id}"
                                      }
                                  """;
    public static final String ORDER_NOT_CANCELABLE_VALUE =
        """
                                      {
                                        "httpMethod": "PATCH",
                                        "httpStatus": "400",
                                        "errorCode": "ORDER_NOT_CANCELABLE",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "취소할 수 없는 주문입니다.",
                                        "path": "/api/v1/orders/{id}"
                                      }
                                  """;
    public static final String ORDER_CANCELLATION_TIMEOUT_VALUE =
        """
                                      {
                                        "httpMethod": "PATCH",
                                        "httpStatus": "400",
                                        "errorCode": "ORDER_CANCELLATION_TIMEOUT",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "주문 생성 후 5분이 경과하여 취소할 수 없습니다.",
                                        "path": "/api/v1/orders/{id}"
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
                                        "path": "/api/v1/orders/{id}"
                                      }
                                  """;
    public static final String ORDER_ALREADY_COMPLETED_VALUE =
        """
                                      {
                                        "httpMethod": "PATCH",
                                        "httpStatus": "409",
                                        "errorCode": "ORDER_ALREADY_COMPLETED",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "완료된 주문은 취소할 수 없습니다.",
                                        "path": "/api/v1/orders/{id}"
                                      }
                                  """;
    public static final String ORDER_PREPARATION_STARTED_VALUE =
        """
                                      {
                                        "httpMethod": "PATCH",
                                        "httpStatus": "409",
                                        "errorCode": "ORDER_PREPARATION_STARTED",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "음식 준비가 시작된 주문은 취소할 수 없습니다.",
                                        "path": "/api/v1/orders/{id}"
                                      }
                                  """;
    public static final String PAYMENT_ALREADY_CANCELED_VALUE =
        """
                                      {
                                        "httpMethod": "PATCH",
                                        "httpStatus": "409",
                                        "errorCode": "PAYMENT_ALREADY_CANCELED",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "이미 취소된 결제입니다.",
                                        "path": "/api/v1/orders/{id}"
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
                                        "path": "/api/v1/orders/{id}"
                                      }
                                  """;
    // 추가된 요청 본문 검증 에러 응답
    public static final String ORDER_REASON_NULL_VALUE =
        """
                                      {
                                        "httpMethod": "PATCH",
                                        "httpStatus": "400",
                                        "errorCode": "ORDER_REASON_NULL",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "취소 이유가 null일 수 없습니다.",
                                        "path": "/api/v1/orders/{id}"
                                      }
                                  """;

    private Cancel() {}
  }

  public final class Create {
    // == 200 OK 응답 ==
    public static final String SUCCESS_DESCRIPTION = "주문 생성 성공";
    public static final String SUCCESS_EXAMPLE =
        """
                  {
                      "id": "3ec24ba4-0164-45d1-9566-73dde029b2c1",
                      "status": "CREATED",
                      "message": "주문이 성공적으로 생성되었습니다."
                  }
              """;

    // == name ==
    public static final String MISSING_STORE_ID_NAME = "가게 ID는 필수입니다";
    public static final String MISSING_ORDER_ITEMS_NAME = "주문 상품은 최소 1개 이상이어야 합니다";
    public static final String STORE_NOT_FOUND_NAME = "존재하지 않는 가게입니다";
    public static final String MULTIPLE_STORE_ORDER_NAME = "주문 상품은 동일한 가게의 상품이어야 합니다.";
    public static final String MISSING_DELIVERY_ADDRESS_NAME = "배송지 주소는 필수입니다";

    // == value ==
    public static final String UNAUTHORIZED_VALUE =
        """
                                  {
                                    "httpMethod": "POST",
                                    "httpStatus": "401",
                                    "errorCode": "UNAUTHORIZED",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                                    "path": "/api/v1/orders"
                                  }
                              """;
    public static final String MISSING_STORE_ID_VALUE =
        """
                                  {
                                    "httpMethod": "POST",
                                    "httpStatus": "400",
                                    "errorCode": "INVALID_INPUT",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "가게 ID는 필수입니다.",
                                    "details": {
                                        "storeId": "가게 ID는 필수입니다"
                                    },
                                    "path": "/api/v1/orders"
                                  }
                              """;
    public static final String MISSING_ORDER_ITEMS_VALUE =
        """
                                  {
                                    "httpMethod": "POST",
                                    "httpStatus": "400",
                                    "errorCode": "INVALID_INPUT",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "주문 상품은 최소 1개 이상이어야 합니다.",
                                    "details": {
                                        "orderItems": "주문 상품은 최소 1개 이상이어야 합니다"
                                    },
                                    "path": "/api/v1/orders"
                                  }
                              """;
    public static final String STORE_NOT_FOUND_VALUE =
        """
                                  {
                                    "httpMethod": "POST",
                                    "httpStatus": "404",
                                    "errorCode": "STORE_NOT_FOUND",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "존재하지 않는 가게입니다.",
                                    "path": "/api/v1/orders"
                                  }
                              """;
    public static final String ORDER_STORE_OWNER_ACCESS_DENIED_VALUE =
        """
                                      {
                                        "httpMethod": "POST",
                                        "httpStatus": "403",
                                        "errorCode": "ORDER_STORE_OWNER_ACCESS_DENIED",
                                        "timestamp": "2025-02-23T15:32:45.123456",
                                        "message": "접근 권한이 없습니다. 해당 매장의 소유자가 아닙니다.",
                                        "path": "/api/v1/orders"
                                      }
                                  """;
    public static final String MULTIPLE_STORE_ORDER_VALUE =
        """
                                  {
                                    "httpMethod": "POST",
                                    "httpStatus": "400",
                                    "errorCode": "MULTIPLE_STORE_ORDER",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "주문 상품은 동일한 가게의 상품이어야 합니다.",
                                    "path": "/api/v1/orders"
                                  }
                              """;
    public static final String MISSING_DELIVERY_ADDRESS_VALUE =
        """
                                  {
                                    "httpMethod": "POST",
                                    "httpStatus": "400",
                                    "errorCode": "MISSING_DELIVERY_ADDRESS",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "배송지 주소는 필수입니다.",
                                    "path": "/api/v1/orders"
                                  }
                              """;

    private Create() {}
  }

  public final class Delete {
    // == 200 OK 응답 ==
    public static final String SUCCESS_DESCRIPTION = "주문 삭제 성공";
    public static final String SUCCESS_EXAMPLE =
        """
              {
                "deletedRequestedAt": "2024-02-19T14:30:00Z"
              }
            """;

    // == name ==
    public static final String ORDER_NOT_DELETABLE_NAME = "주문 과정이 완료되지 않아 삭제할 수 없는 주문입니다.";

    // == value ==
    public static final String UNAUTHORIZED_VALUE =
        """
                                  {
                                    "httpMethod": "DELETE",
                                    "httpStatus": "401",
                                    "errorCode": "UNAUTHORIZED",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                                    "path": "/api/v1/orders/{id}"
                                  }
                              """;
    public static final String NOT_FOUND_ORDER_VALUE =
        """
                                  {
                                    "httpMethod": "DELETE",
                                    "httpStatus": "404",
                                    "errorCode": "NOT_FOUND_ORDER",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "해당 주문을 찾을 수 없습니다.",
                                    "path": "/api/v1/orders/{id}"
                                  }
                              """;
    public static final String ORDER_NOT_DELETABLE_VALUE =
        """
                                  {
                                    "httpMethod": "DELETE",
                                    "httpStatus": "409",
                                    "errorCode": "ORDER_NOT_DELETABLE",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "주문 과정이 완료되지 않아 삭제할 수 없는 주문입니다.",
                                    "path": "/api/v1/orders/{id}"
                                  }
                              """;

    private Delete() {}
  }

  public final class UpdateStatus {
    // == 200 OK 응답 ==
    public static final String SUCCESS_DESCRIPTION = "주문 상태 업데이트 성공";
    public static final String SUCCESS_EXAMPLE =
        """
              {
                "orderId": "123e4567-e89b-12d3-a456-426614174000",
                "type": "온라인 주문",
                "status": "음식 준비 중",
                "updatedAt": "2024-02-12T15:30:00"
              }
            """;

    // == name ==
    public static final String ORDER_STATUS_CHANGE_NOT_OWNER_NAME = "본인 가게의 주문만 상태를 변경할 수 있습니다.";
    public static final String ORDER_STATUS_CHANGE_FORBIDDEN_NAME = "고객은 주문 상태를 변경할 수 없습니다.";
    public static final String ORDER_CANCEL_SEPARATE_API_NAME =
        "주문 취소는 별도의 취소 요청(/api/v1/orders/{id} PATCH) 통해서만 가능합니다.";
    public static final String ORDER_PAID_SEPARATE_NAME = "주문 결제 완료는 결제 요청 후 변경 가능한 상태입니다.";
    public static final String ORDER_STATUS_NULL_NAME = "주문 상태는 필수입니다.";
    public static final String ORDER_FINALIZED_STATE_CONFLICT_NAME =
        "취소되거나, 완료된 주문의 상태를 변경할 수 없습니다.";
    public static final String ORDER_STATUS_REGRESSION_NAME = "주문 상태를 이전 단계로 변경할 수 없습니다.";
    public static final String OFFLINE_ORDER_DELIVERY_STATUS_NAME = "매장 주문은 배달 관련 상태로 변경할 수 없습니다.";

    // == value ==
    public static final String UNAUTHORIZED_VALUE =
        """
                                  {
                                    "httpMethod": "PATCH",
                                    "httpStatus": "401",
                                    "errorCode": "UNAUTHORIZED",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "로그인이 필요한 서비스입니다. 로그인 후 다시 시도해주세요.",
                                    "path": "/api/v1/orders/{id}/status"
                                  }
                              """;

    // == 에러 응답 예시 ==
    public static final String NOT_FOUND_ORDER_VALUE =
        """
                                  {
                                    "httpMethod": "PATCH",
                                    "httpStatus": "404",
                                    "errorCode": "NOT_FOUND_ORDER",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "해당 주문을 찾을 수 없습니다.",
                                    "path": "/api/v1/orders/{id}/status"
                                  }
                              """;
    public static final String ORDER_STATUS_CHANGE_NOT_OWNER_VALUE =
        """
                                  {
                                    "httpMethod": "PATCH",
                                    "httpStatus": "403",
                                    "errorCode": "ORDER_STATUS_CHANGE_NOT_OWNER",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "본인 가게의 주문만 상태를 변경할 수 있습니다.",
                                    "path": "/api/v1/orders/{id}/status"
                                  }
                              """;
    public static final String ORDER_STATUS_CHANGE_FORBIDDEN_VALUE =
        """
                                  {
                                    "httpMethod": "PATCH",
                                    "httpStatus": "403",
                                    "errorCode": "ORDER_STATUS_CHANGE_FORBIDDEN",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "고객은 주문 상태를 변경할 수 없습니다.",
                                    "path": "/api/v1/orders/{id}/status"
                                  }
                              """;
    public static final String ORDER_CANCEL_SEPARATE_API_VALUE =
        """
                                  {
                                    "httpMethod": "PATCH",
                                    "httpStatus": "400",
                                    "errorCode": "ORDER_CANCEL_SEPARATE_API",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "주문 취소는 별도의 취소 요청(/api/v1/orders/{id} PATCH) 통해서만 가능합니다.",
                                    "path": "/api/v1/orders/{id}/status"
                                  }
                              """;
    public static final String ORDER_PAID_SEPARATE_VALUE =
        """
                                  {
                                    "httpMethod": "PATCH",
                                    "httpStatus": "400",
                                    "errorCode": "ORDER_PAID_SEPARATE",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "주문 결제 완료는 결제 요청 후 변경 가능한 상태입니다.",
                                    "path": "/api/v1/orders/{id}/status"
                                  }
                              """;
    public static final String ORDER_STATUS_NULL_VALUE =
        """
                                  {
                                    "httpMethod": "PATCH",
                                    "httpStatus": "400",
                                    "errorCode": "ORDER_STATUS_NULL",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "주문 상태가 null일 수 없습니다.",
                                    "path": "/api/v1/orders/{id}/status"
                                  }
                              """;
    public static final String ORDER_FINALIZED_STATE_CONFLICT_VALUE =
        """
                                  {
                                    "httpMethod": "PATCH",
                                    "httpStatus": "409",
                                    "errorCode": "ORDER_FINALIZED_STATE_CONFLICT",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "취소되거나, 완료된 주문의 상태를 변경할 수 없습니다.",
                                    "path": "/api/v1/orders/{id}/status"
                                  }
                              """;
    public static final String ORDER_STATUS_REGRESSION_VALUE =
        """
                                  {
                                    "httpMethod": "PATCH",
                                    "httpStatus": "409",
                                    "errorCode": "ORDER_STATUS_REGRESSION",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "주문 상태를 이전 단계로 변경할 수
                              "주문 상태를 이전 단계로 변경할 수 없습니다.",
                              "path": "/api/v1/orders/{id}/status"
                            }
                        """;
    public static final String OFFLINE_ORDER_DELIVERY_STATUS_VALUE =
        """
                                  {
                                    "httpMethod": "PATCH",
                                    "httpStatus": "409",
                                    "errorCode": "OFFLINE_ORDER_DELIVERY_STATUS",
                                    "timestamp": "2025-02-23T15:32:45.123456",
                                    "message": "매장 주문은 배달 관련 상태로 변경할 수 없습니다.",
                                    "path": "/api/v1/orders/{id}/status"
                                  }
                              """;

    private UpdateStatus() {}
  }
}
