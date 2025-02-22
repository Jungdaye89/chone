package com.chone.server.domains.user.facade;

import com.chone.server.commons.exception.ApiBusinessException;
import com.chone.server.commons.facade.UserFacade;
import com.chone.server.domains.user.domain.User;
import com.chone.server.domains.user.exception.UserExceptionCode;
import com.chone.server.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {
  private final UserRepository userRepository;

  @Override
  public User findUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new ApiBusinessException(UserExceptionCode.USER_NOT_FOUND));
  }
}
