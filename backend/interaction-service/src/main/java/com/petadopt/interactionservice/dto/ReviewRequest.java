package com.petadopt.interactionservice.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long userId;
    private Long targetId;
    private Integer rating;
    private String comment;
}
