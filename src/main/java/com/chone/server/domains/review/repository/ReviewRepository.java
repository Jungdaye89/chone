package com.chone.server.domains.review.repository;

import com.chone.server.domains.review.domain.Review;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, UUID> {

  Optional<Review> findByOrderId(UUID orderId);

  Optional<Review> findByIdAndDeletedAtIsNull(UUID id);
}
