package com.chone.server.domains.user.repository;

import com.chone.server.domains.user.domain.User;
import com.chone.server.domains.user.repository.custom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
  Optional<User> findByIdAndDeletedAtIsNull(Long userId);

  Optional<User> findByUsername(String username);

  Optional<User> findByUsernameAndDeletedAtIsNull(String username);

  Optional<User> findByEmail(String email);
}
