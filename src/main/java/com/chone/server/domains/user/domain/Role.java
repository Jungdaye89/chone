package com.chone.server.domains.user.domain;

public enum Role {
  CUSTOMER(Authority.CUSTOMER),
  OWNER(Authority.OWNER),
  MANAGER(Authority.MANAGER),
  MASTER(Authority.MASTER);

  private final String authority;

  Role(String authority) {
    this.authority = authority;
  }

  public String getAuthority() {
    return this.authority;
  }

  public boolean isCustomer() {
    return this == Role.CUSTOMER;
  }

  public boolean isOwner() {
    return this == Role.OWNER;
  }

  public boolean isManager() {
    return this == Role.MANAGER;
  }

  public boolean isMaster() {
    return this == Role.MASTER;
  }

  public static class Authority {
    public static final String CUSTOMER = "ROLE_CUSTOMER";
    public static final String OWNER = "ROLE_OWNER";
    public static final String MANAGER = "ROLE_MANAGER";
    public static final String MASTER = "ROLE_MASTER";
  }
}
