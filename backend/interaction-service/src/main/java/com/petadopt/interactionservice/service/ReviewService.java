package com.petadopt.interactionservice.service;

import com.petadopt.interactionservice.dto.ReviewRequest;
import com.petadopt.interactionservice.exception.ResourceNotFoundException;
import com.petadopt.interactionservice.model.Review;
import com.petadopt.interactionservice.repository.ReviewRepository;
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
                .userId(request.getUserId())
                .targetId(request.getTargetId())
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return reviewRepository.save(review);
    }

    public Review getReviewById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id: " + id));
    }

    public List<Review> getReviewsByTargetId(Long targetId) {
        return reviewRepository.findByTargetId(targetId);
    }

    public List<Review> getReviewsByUserId(Long userId) {
        return reviewRepository.findByUserId(userId);
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
