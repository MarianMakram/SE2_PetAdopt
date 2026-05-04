package com.petadopt.interactionservice.controllers;

import com.petadopt.interactionservice.dto.ReviewRequest;
import com.petadopt.interactionservice.models.Review;
import com.petadopt.interactionservice.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest request) {
        return new ResponseEntity<>(reviewService.createReview(request), HttpStatus.CREATED);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Review>> getReviewsByPetId(@PathVariable Long petId) {
        return ResponseEntity.ok(reviewService.getReviewsByPetId(petId));
    }

    @GetMapping("/adopter/{adopterId}")
    public ResponseEntity<List<Review>> getReviewsByAdopterId(@PathVariable Long adopterId) {
        return ResponseEntity.ok(reviewService.getReviewsByAdopterId(adopterId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Review> updateReview(@PathVariable Long id, @RequestBody ReviewRequest request) {
        return ResponseEntity.ok(reviewService.updateReview(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
