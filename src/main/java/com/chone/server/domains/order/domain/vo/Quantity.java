package com.chone.server.domains.order.domain.vo;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.exception.OrderExceptionCode;

public record Quantity(int value) {

  public Quantity {
    if (value <= 0) {
      throw new ApiBusinessException(OrderExceptionCode.INVALID_QUANTITY);
    }
  }
}
