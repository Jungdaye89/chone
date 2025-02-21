package com.chone.server.commons.facade;

import com.chone.server.domains.user.domain.User;

public interface UserFacade {
    User findUserById(Long id);
}
