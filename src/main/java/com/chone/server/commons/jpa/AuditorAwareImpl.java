package com.chone.server.commons.jpa;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.user.domain.User;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuditorAwareImpl implements AuditorAware<String> {
  @Override
  public Optional<String> getCurrentAuditor() {
    try {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication == null
          || !authentication.isAuthenticated()
          || authentication instanceof AnonymousAuthenticationToken) {
        return Optional.of("system");
      }

      Object principal = authentication.getPrincipal();
      if (principal instanceof CustomUserDetails) {
        User user = ((CustomUserDetails) principal).getUser();
        return Optional.of(user.getUsername());
      }

      return Optional.of("system");
    } catch (Exception e) {
      return Optional.of("system");
    }
  }
}
