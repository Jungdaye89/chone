package com.chone.server.domains.order.domain.vo;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import java.math.BigDecimal;

public record Price(BigDecimal value) {
  public static final Price ZERO = new Price(BigDecimal.ZERO);

  public Price {
    if (value.compareTo(BigDecimal.ZERO) < 0) {
      throw new ApiBusinessException(OrderExceptionCode.NEGATIVE_PRICE);
    }
  }

  public Price add(Price other) {
    return new Price(this.value.add(other.value));
  }

  public Price multiply(int quantity) {
    return new Price(this.value.multiply(BigDecimal.valueOf(quantity)));
  }
}
