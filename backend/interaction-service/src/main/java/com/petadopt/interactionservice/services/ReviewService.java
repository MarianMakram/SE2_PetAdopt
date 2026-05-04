package com.petadopt.interactionservice.services;

import com.petadopt.interactionservice.dto.ReviewRequest;
import com.petadopt.interactionservice.exception.ResourceNotFoundException;
import com.petadopt.interactionservice.models.Review;
import com.petadopt.interactionservice.repositories.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review createReview(ReviewRequest request) {
        if (request.getRating() < 1 || request.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = Review.builder()
                .adopterId(request.getAdopterId())
                .petId(request.getPetId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByPetId(Long petId) {
        return reviewRepository.findByPetId(petId);
    }

    public List<Review> getReviewsByAdopterId(Long adopterId) {
        return reviewRepository.findByAdopterId(adopterId);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    public Review updateReview(Long id, ReviewRequest request) {
        Review existingReview = getReviewById(id);

        if (request.getRating() != null) {
            if (request.getRating() < 1 || request.getRating() > 5) {
                throw new IllegalArgumentException("Rating must be between 1 and 5");
            }
            existingReview.setRating(request.getRating());
        }

        if (request.getComment() != null) {
            existingReview.setComment(request.getComment());
        }

        return reviewRepository.save(existingReview);
    }

    public void deleteReview(Long id) {
        if (!reviewRepository.existsById(id)) {
            throw new ResourceNotFoundException("Review not found with id: " + id);
        }
        reviewRepository.deleteById(id);
    }
}
