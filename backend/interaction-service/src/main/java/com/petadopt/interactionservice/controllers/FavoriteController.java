package com.petadopt.interactionservice.controllers;

import com.petadopt.interactionservice.dto.FavoriteRequest;
import com.petadopt.interactionservice.models.Favorite;
import com.petadopt.interactionservice.services.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping
    public ResponseEntity<Favorite> addFavorite(@RequestBody FavoriteRequest request) {
        return new ResponseEntity<>(favoriteService.addFavorite(request), HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Favorite>> getUserFavorites(@PathVariable Long userId) {
        return ResponseEntity.ok(favoriteService.getUserFavorites(userId));
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestParam Long userId, @RequestParam Long petId) {
        favoriteService.removeFavorite(userId, petId);
        return ResponseEntity.noContent().build();
    }
}
