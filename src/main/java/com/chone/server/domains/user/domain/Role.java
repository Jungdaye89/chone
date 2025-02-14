package com.chone.server.domains.user.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    CUSTOMER, OWNER, MANAGER, MASTER
}
