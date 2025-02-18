package com.chone.server.domains.order.repository;

import com.chone.server.domains.order.dto.response.OrderDetailResponse;
import com.chone.server.domains.user.domain.User;
import java.util.UUID;

public interface OrderDetailSearchRepository {
  OrderDetailResponse findOrderDetailByIdForCustomer(UUID orderId, User customerUser);

  OrderDetailResponse findOrderDetailByIdForOwner(UUID orderId, User customerUser);

  OrderDetailResponse findOrderDetailByIdForAdmin(UUID orderId);
}
