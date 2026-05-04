package com.petadopt.interactionservice.dto;

import lombok.Data;

@Data
public class ReviewRequest {
    private Long adopterId;
    private Long petId;
    private Integer rating;
    private String comment;
}
