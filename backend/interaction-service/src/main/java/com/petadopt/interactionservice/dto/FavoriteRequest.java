package com.petadopt.interactionservice.dto;

import lombok.Data;

@Data
public class FavoriteRequest {
    private Long userId;
    private Long petId;
}
