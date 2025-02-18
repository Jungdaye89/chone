package com.chone.server.domains.order.domain;

public enum OrderStatus {
  PENDING,
  ACCEPTED,
  FOOD_PREPARING,
  FOOD_PREPARED,
  AWAITING_DELIVERY,
  IN_DELIVERY,
  COMPLETED,
  CANCELED
}
