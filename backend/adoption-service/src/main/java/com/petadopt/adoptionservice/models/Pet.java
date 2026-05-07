package com.petadopt.adoptionservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet {
    private int id;
    private int ownerId;
    private String name;
    private String breed;
    private int age;
    private int ageUnit;
    private String species;
    private String gender;
    private String status;
    private String description;
    private String healthStatus;
    private String location;
    private String imageUrls;
    private String createdAt;
}
