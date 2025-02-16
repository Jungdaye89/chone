package com.chone.server.commons.config;

import com.chone.server.domains.auth.filter.JwtFilter;
import com.chone.server.domains.auth.filter.LoginFilter;
import com.chone.server.domains.auth.service.MockUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig {
  private final AuthenticationConfiguration authenticationConfiguration;
  private final MockUserDetailsServiceImpl userDetailsServiceImpl; // 구현 완료 시 교체가 필요합니다.

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf((csrf) -> csrf.disable());

    http.sessionManagement(
        (sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    http.authorizeHttpRequests(
        (authorizeHttpRequests) -> authorizeHttpRequests.anyRequest().authenticated());

    http.addFilterBefore(jwtAuthorizationFilter(), LoginFilter.class);
    http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public JwtFilter jwtAuthorizationFilter() {
    return new JwtFilter(userDetailsServiceImpl);
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public LoginFilter jwtAuthenticationFilter() throws Exception {
    LoginFilter filter = new LoginFilter(userDetailsServiceImpl);
    filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
    return filter;
  }
}
