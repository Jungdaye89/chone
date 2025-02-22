package com.chone.server.domains.order.service;

import com.chone.server.domains.order.domain.Order;
import lombok.Getter;

@Getter
public class OrderCancelledEvent {
  private final Order order;
  private final boolean isByUser;

  public OrderCancelledEvent(Order order, boolean isByUser) {
    this.order = order;
    this.isByUser = isByUser;
  }
}
