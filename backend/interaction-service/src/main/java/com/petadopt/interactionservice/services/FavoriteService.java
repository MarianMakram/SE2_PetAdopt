package com.petadopt.interactionservice.services;

import com.petadopt.interactionservice.dto.FavoriteRequest;
import com.petadopt.interactionservice.exception.ResourceNotFoundException;
import com.petadopt.interactionservice.models.Favorite;
import com.petadopt.interactionservice.repositories.FavoriteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;

    public Favorite addFavorite(FavoriteRequest request) {
        if (favoriteRepository.existsByUserIdAndPetId(request.getUserId(), request.getPetId())) {
            throw new IllegalArgumentException("Favorite already exists");
        }
        
        Favorite favorite = Favorite.builder()
                .userId(request.getUserId())
                .petId(request.getPetId())
                .build();
                
        return favoriteRepository.save(favorite);
    }

    public List<Favorite> getUserFavorites(Long userId) {
        return favoriteRepository.findByUserId(userId);
    }

    public void removeFavorite(Long userId, Long petId) {
        Favorite favorite = favoriteRepository.findByUserIdAndPetId(userId, petId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite not found"));
        favoriteRepository.delete(favorite);
    }
}
