package com.petadopt.interactionservice.repositories;

import com.petadopt.interactionservice.models.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByPetId(Long petId);
    List<Review> findByAdopterId(Long adopterId);
}
