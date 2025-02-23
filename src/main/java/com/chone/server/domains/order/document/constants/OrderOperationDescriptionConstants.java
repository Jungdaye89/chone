package com.chone.server.domains.order.document.constants;

public final class OrderOperationDescriptionConstants {
  // API Summaries
  public static final String LIST_SUMMARY = "주문 조회 API";
  public static final String CREATE_SUMMARY = "주문 생성 API";
  public static final String CANCEL_SUMMARY = "주문 취소 API";
  public static final String DELETE_SUMMARY = "주문 삭제 API";
  public static final String DETAIL_SUMMARY = "주문 상세 조회 API";
  public static final String UPDATE_STATUS_SUMMARY = "주문 상태 업데이트 API";

  // API Descriptions
  public static final String LIST_DESCRIPTION =
      """
              - 사용자별 또는 가게별로 주문을 조회할 수 있는 API입니다.
                - 메서드: GET
                - 경로: /api/v1/orders
                - 권한: 모든 역할 접근 가능 (역할별 조회 범위 제한)
                - 페이지네이션: page, size, sort 지원 (기본값: page=0, size=10, sort=createdat,desc)
                - 응답: 200 OK + PageResponse<OrderPageResponse>

              역할별 조회 가능 범위 및 필터링 제한
              - 역할: CUSTOMER
                - 조회 가능 범위: 자신의 주문만 조회
                - 필터링 제한: 사용자 필터링 불가, 가게 필터링 가능
              - 역할: OWNER
                - 조회 가능 범위: 자신의 매장 주문만 조회
                - 필터링 제한: 사용자 필터링 가능, 가게 필터링 불가
              - 역할: MANAGER, MASTER
                - 조회 가능 범위: 모든 주문 조회
                - 필터링 제한: 모든 필터링 가능
              """;
  public static final String CANCEL_DESCRIPTION =
      """
         - 사용자가 주문을 취소할 수 있는 API입니다.
           - 메서드: PATCH
           - 경로: /api/orders/{id}
           - 권한: 모든 역할 접근 가능 (역할별 권한 제한)
           - 파라미터: id (주문 UUID), 요청 바디
           - 응답: 200 OK

         역할별 접근 제어
         - 역할: CUSTOMER
           - 취소 가능 범위: 자신의 주문만 취소 가능
         - 역할: OWNER
           - 취소 가능 범위: 자신의 매장 주문만 취소 가능
         - 역할: MANAGER, MASTER
           - 취소 가능 범위: 모든 주문 취소 가능
         """;
  public static final String CREATE_DESCRIPTION =
      """
              - 사용자가 주문을 생성할 수 있는 API입니다.
                - 메서드: POST
                - 경로: /api/v1/orders
                - 권한: CUSTOMER, STORE_OWNER 역할만 접근 가능
                - 응답: 201 Created
                - Location 헤더: 생성된 리소스 URI
            """;
  public static final String DELETE_DESCRIPTION =
      """
              - 항목: 내용
                - 메서드: DELETE
                - 경로: /api/orders/{id}
                - 권한: MANAGER, MASTER 역할만 접근 가능
                - 파라미터: id (주문 UUID)
                - 응답: 200 OK

              역할별 접근 제어
              - 역할: MANAGER, MASTER
                - 삭제 가능 범위: 주문 상태가 취소 또는 완료된 주문만 삭제 가능
          """;

  public static final String DETAIL_DESCRIPTION =
      """
            - 사용자 역할(고객, 가게 주인, 관리자)에 따라 주문 상세 정보를 조회할 수 있습니다.
              - 메서드: GET
              - 경로: /api/orders/{id}
              - 권한: 모든 역할 접근 가능 (역할별 조회 범위 제한)
              - 파라미터: id (주문 UUID)
              - 응답: 200 OK

            역할별 접근 제어
            - 역할: CUSTOMER
              - 조회 가능 범위: 자신의 주문만 조회
            - 역할: OWNER
              - 조회 가능 범위: 자신의 매장 주문만 조회
            - 역할: MANAGER, MASTER
              - 조회 가능 범위: 모든 주문 조회
          """;
  public static final String UPDATE_DESCRIPTION =
      """
             - 사용자가 주문 상태를 업데이트할 수 있는 API입니다.
               - 메서드: PATCH
               - 경로: /api/v1/orders/{id}/status
               - 권한: CUSTOMER 역할 제외, 가게 주인은 본인 가게 주문만 가능
               - 응답: 200 OK
          """;

  private OrderOperationDescriptionConstants() {}
}
