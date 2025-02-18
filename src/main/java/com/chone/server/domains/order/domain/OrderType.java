package com.chone.server.domains.order.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderType {
  ONLINE("배달"),
  OFFLINE("대면");

  private final String description;
}
