package com.petadopt.interactionservice.repository;

import com.petadopt.interactionservice.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByTargetId(Long targetId);
    List<Review> findByUserId(Long userId);
}
