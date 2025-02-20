package com.chone.server.domains.user.repository;

import com.chone.server.domains.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
