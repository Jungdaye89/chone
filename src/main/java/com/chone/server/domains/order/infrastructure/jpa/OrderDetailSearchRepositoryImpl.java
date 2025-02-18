package com.chone.server.domains.order.infrastructure.jpa;

import static com.chone.server.domains.store.domain.QStore.store;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.dto.response.OrderDetailResponse;
import com.chone.server.domains.order.dto.response.OrderDetailResponse.OrderItemResponse;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import com.chone.server.domains.order.repository.OrderDetailSearchRepository;
import com.chone.server.domains.store.domain.Store;
import com.chone.server.domains.user.domain.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderDetailSearchRepositoryImpl implements OrderDetailSearchRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public OrderDetailResponse findOrderDetailByIdForCustomer(UUID orderId, User customerUser) {
    OrderDetailResponse response = findOrderDetailById(orderId);

    if (!customerUser.getId().equals(response.user().id())) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_CUSTOMER_ACCESS_DENIED);
    }

    return response;
  }

  @Override
  public OrderDetailResponse findOrderDetailByIdForOwner(UUID orderId, User ownerUser) {
    OrderDetailResponse response = findOrderDetailById(orderId);

    Store userStore =
        queryFactory
            .selectFrom(store)
            .where(store.id.eq(response.store().id()), store.user.id.eq(ownerUser.getId()))
            .fetchOne();

    if (userStore == null) {
      throw new ApiBusinessException(OrderExceptionCode.ORDER_STORE_OWNER_ACCESS_DENIED);
    }

    return response;
  }

  @Override
  public OrderDetailResponse findOrderDetailByIdForAdmin(UUID orderId) {
    return findOrderDetailById(orderId);
  }

  private OrderDetailResponse findOrderDetailById(UUID orderId) {

    Tuple mainData = fetchOrderMainData(orderId);

    if (mainData == null) {
      throw new ApiBusinessException(OrderExceptionCode.NOT_FOUND_ORDER);
    }

    OrderDetailResponse.OrderResponse orderResponseData =
        mainData.get(0, OrderDetailResponse.OrderResponse.class);
    OrderDetailResponse.UserResponse userResponseData =
        mainData.get(1, OrderDetailResponse.UserResponse.class);
    OrderDetailResponse.StoreResponse storeResponseData =
        mainData.get(2, OrderDetailResponse.StoreResponse.class);

    List<OrderItemResponse> orderItems = fetchOrderItems(orderId);

    return new OrderDetailResponse(
        orderResponseData, userResponseData, storeResponseData, orderItems);
  }

  private Tuple fetchOrderMainData(UUID orderId) {
    return null;
  }

  private List<OrderItemResponse> fetchOrderItems(UUID orderId) {
    return null;
  }
}
