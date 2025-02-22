package com.chone.server.domains.order.service;

import com.chone.server.domains.order.domain.Order;
import com.chone.server.domains.user.domain.User;
import lombok.Getter;

@Getter
public class OrderDeletedEvent {
  private final Order order;
  private final User deletedBy;
  private final boolean isByUser;

  public OrderDeletedEvent(Order order, User deletedBy, boolean isByUser) {
    this.order = order;
    this.deletedBy = deletedBy;
    this.isByUser = isByUser;
  }
}
