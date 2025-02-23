package com.chone.server.domains.payment.document.constants;

public final class PaymentOperationDescriptionConstants {
  // API Summaries
  public static final String LIST_SUMMARY = "결제 목록 조회 API";
  public static final String DETAIL_SUMMARY = "결제 상세 조회 API";
  public static final String CANCEL_SUMMARY = "결제 취소 API";
  public static final String CREATE_SUMMARY = "결제 생성 API";

  // API Descriptions
  public static final String LIST_DESCRIPTION =
      """
            - 결제 내역을 조회할 수 있는 API입니다.
                - 메서드: GET
                - 경로: /api/v1/payments
                - 권한: 모든 역할 접근 가능 (역할별 조회 범위 제한)

            - 필터링 파라미터
                - startDate: 조회 시작일 (yyyy-MM-dd)
                - endDate: 조회 종료일 (yyyy-MM-dd)
                - storeId: 가게 ID
                - userId: 사용자 ID
                - status: 결제 상태 (PENDING, COMPLETED, CANCELED, FAILED)
                - method: 결제 수단 (CASH, CARD)
                - totalPrice: 정확한 결제 금액
                - minPrice: 최소 결제 금액
                - maxPrice: 최대 결제 금액

            - 페이지네이션
                - page: 페이지 번호 (0부터 시작, 기본값: 0)
                - size: 페이지 크기 (기본값: 10)
                - sort: 정렬 기준 (기본값: createdat,desc)
                    - 정렬 가능 필드: createdat, updatedat
                    - 정렬 방향: asc, desc

            - 역할별 조회 가능 범위
                - CUSTOMER
                    - 조회 가능 범위: 자신의 결제만 조회
                    - 필터링 제한: 고객 필터링 불가, 매장 필터링 가능
                - OWNER
                    - 조회 가능 범위: 자신의 매장 결제만 조회
                    - 필터링 제한: 고객 필터링 가능, 매장 필터링 불가
                - MANAGER, MASTER
                    - 조회 가능 범위: 모든 결제 조회
                    - 필터링 제한: 모든 필터링 가능
            """;
  public static final String DETAIL_DESCRIPTION =
      """
    - 결제 ID에 대한 결제 내역을 조회하는 API입니다.
    - 메서드: **GET**
    - 경로: /api/payments/{id}
    - 권한: 모든 역할 접근 가능 (역할별 조회 범위 제한)
    - 파라미터: id (결제 UUID)
    - 응답: 200 OK

    - 역할: 조회 가능 범위
        - CUSTOMER: 자신의 결제만 조회
        - OWNER: 자신의 매장 결제만 조회
        - MANAGER, MASTER: 모든 결제 조회
    """;
  public static final String CANCEL_DESCRIPTION =
      """
    - 결제를 취소할 수 있는 API입니다.
        - 메서드: **PATCH**
        - 경로: /api/v1/payments/{id}
        - 권한: `CUSTOMER`, `OWNER`, `MANAGER`, `MASTER` 역할 접근 가능
        - 요청: `CancelPaymentRequest` (검증 어노테이션 적용)
        - 응답: 200 OK + `CancelPaymentResponse`

    - 역할: 취소 가능 범위
        - `CUSTOMER`: 자신의 결제만 취소 가능
        - `OWNER`: 자신의 매장 결제만 취소 가능
        - `MANAGER`, `MASTER`: 모든 결제 취소 가능
    """;
  public static final String CREATE_DESCRIPTION =
      """
    - 주문에 대한 결제를 생성하는 API입니다.
        - 메서드: **POST**
        - 경로: /api/v1/payments
        - 권한: `CUSTOMER`, `OWNER`, `MANAGER`, `MASTER` 역할 접근 가능
        - 요청: `CreatePaymentRequest` (검증 어노테이션 적용)
        - 응답: 201 Created
        - Location 헤더: 생성된 결제 리소스 URI
    """;

  private PaymentOperationDescriptionConstants() {}
}
