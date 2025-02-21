package com.chone.server.domains.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
  ONLINE("배달"),
  OFFLINE("대면");

  private final String description;

  public boolean isOnline() {
    return this == OrderType.ONLINE;
  }

  public boolean isOffline() {
    return this == OrderType.ONLINE;
  }
}
