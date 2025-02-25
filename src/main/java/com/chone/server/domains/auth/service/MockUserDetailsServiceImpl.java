package com.chone.server.domains.auth.service;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.user.domain.Role;
import com.chone.server.domains.user.domain.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MockUserDetailsServiceImpl {
//  private static final String MOCK_ROLE_HEADER = "X-Mock-Role";
//
//  public CustomUserDetails loadUserFromHeader(HttpServletRequest request) {
//    String mockRole = request.getHeader(MOCK_ROLE_HEADER);
//    if (mockRole == null) {
//      throw new UsernameNotFoundException("Header not found");
//    }
//
//    try {
//      Role role = Role.valueOf(mockRole.toUpperCase());
//      return createMockUser(role);
//    } catch (IllegalArgumentException e) {
//      throw new UsernameNotFoundException("Invalid role: " + mockRole);
//    }
//  }
//
//  @Override
//  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    return createMockUser(Role.CUSTOMER);
//  }
//
//  private CustomUserDetails createMockUser(Role role) {
//    User mockUser =
//        switch (role) {
//          case CUSTOMER ->
//              User.builder("customer", "customer@example.com", "customerPass", Role.CUSTOMER, true)
//                  .id(1000L)
//                  .build();
//          case OWNER ->
//              User.builder("owner", "owner@example.com", "ownerPass", Role.OWNER, true)
//                  .id(2000L)
//                  .build();
//          case MANAGER ->
//              User.builder("manager", "manager@example.com", "managerPass", Role.MANAGER, true)
//                  .id(3000L)
//                  .build();
//          case MASTER ->
//              User.builder("master", "master@example.com", "masterPass", Role.MASTER, true)
//                  .id(4000L)
//                  .build();
//        };
//
//    return new CustomUserDetails(mockUser);
//  }
}
