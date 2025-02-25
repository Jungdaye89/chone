package com.chone.server.domains.ai.repository;

import com.chone.server.domains.ai.domain.Ai;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiRepository extends JpaRepository<Ai, UUID> {}
