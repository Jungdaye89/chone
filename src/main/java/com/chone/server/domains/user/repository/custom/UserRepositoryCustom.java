package com.chone.server.domains.user.repository.custom;

import com.chone.server.domains.auth.dto.CustomUserDetails;
import com.chone.server.domains.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface UserRepositoryCustom {
    Page<User> findUsers(String username, String email, String role,
                         LocalDate startDate, LocalDate endDate, Pageable pageable);
}
