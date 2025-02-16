package com.chone.server.domains.auth.filter;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.auth.service.MockUserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

  private static final String USER_LOGIN_ENDPOINT = "/api/user/login";
  private final UserDetailsService userDetailsService;

  public LoginFilter(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
    setFilterProcessesUrl(USER_LOGIN_ENDPOINT);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
      throws AuthenticationException {
    // mock user
    CustomUserDetails user =
        ((MockUserDetailsServiceImpl) userDetailsService).loadUserFromHeader(req);

    return getAuthenticationManager()
        .authenticate(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult) {}

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
