package com.petadopt.adoptionservice.models;

import com.petadopt.adoptionservice.models.enums.RequestStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "adoptions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adoption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int petId;
    private int adopterId;
    private int ownerId;

    private String message;
    private String whyThisPet;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    private String rejectionReason;
    private LocalDateTime respondedAt;
    private LocalDateTime createdAt;

    @Transient
    private Pet pet;

    @Transient
    private User adopter;

    @Builder.Default
    private LocalDateTime requestedAt = LocalDateTime.now();



    @PrePersist
    protected void onCreate() {

        this.createdAt = LocalDateTime.now();
        if (this.requestedAt == null) {
            this.requestedAt = LocalDateTime.now();
        }
        if (this.status == null) {
            this.status = RequestStatus.PENDING;
        }
    }
}
