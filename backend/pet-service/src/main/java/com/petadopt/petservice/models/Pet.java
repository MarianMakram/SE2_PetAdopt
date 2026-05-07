package com.petadopt.petservice.models;

import com.petadopt.petservice.models.enums.Gender;
import com.petadopt.petservice.models.enums.PetStatus;
import com.petadopt.petservice.models.enums.Species;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "pets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long ownerId;

    @Column(nullable = false)
    private String name;

    private String breed;

    private Integer age;
    private Integer ageUnit; // 0 for Months, 1 for Years (optional mapping)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Species species;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetStatus status;

    @Column(length = 2000)
    private String description;

    private String healthStatus;
    private String location;

    @Column(length = 2000)
    private String imageUrls;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = PetStatus.PENDING_REVIEW;
        }
    }
}
