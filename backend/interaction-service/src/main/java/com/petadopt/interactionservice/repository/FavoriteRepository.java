package com.petadopt.interactionservice.repository;

import com.petadopt.interactionservice.model.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    List<Favorite> findByUserId(Long userId);
    Optional<Favorite> findByUserIdAndPetId(Long userId, Long petId);
    boolean existsByUserIdAndPetId(Long userId, Long petId);
}
