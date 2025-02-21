package com.chone.server.domains.common.vo;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.domains.order.exception.OrderExceptionCode;
import java.util.List;
import java.util.Objects;

public record Price(int value) {
  public static final Price ZERO = new Price(0);

  public Price {
    if (value < 0) {
      throw new ApiBusinessException(OrderExceptionCode.NEGATIVE_PRICE);
    }
  }

  public static Price sum(List<Price> prices) {
    int total = prices.stream().mapToInt(Price::value).sum();
    return new Price(total);
  }

  public Price add(Price other) {
    return new Price(this.value + other.value);
  }

  public Price multiply(int quantity) {
    return new Price(this.value * quantity);
  }

  public Price subtract(Price other) {
    return new Price(this.value - other.value);
  }

  public boolean isGreaterThan(Price other) {
    return this.value > other.value;
  }

  public boolean isLessThan(Price other) {
    return this.value < other.value;
  }

  public boolean isEqualTo(Price other) {
    return this.value == other.value;
  }

  public String formatted() {
    return value + "원";
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Price price = (Price) o;
    return value == price.value;
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }
}
