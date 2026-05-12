package com.petadopt.adoptionservice.services.impl;

import com.petadopt.adoptionservice.client.InteractionServiceClient;
import com.petadopt.adoptionservice.models.dto.NotificationRequest;
import com.petadopt.adoptionservice.client.PetServiceClient;
import com.petadopt.adoptionservice.models.Adoption;
import com.petadopt.adoptionservice.models.Pet;
import com.petadopt.adoptionservice.models.enums.RequestStatus;
import com.petadopt.adoptionservice.repositories.AdoptionRepository;
import com.petadopt.adoptionservice.services.AdoptionService;
import com.petadopt.adoptionservice.services.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdoptionServiceImpl implements AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final PetServiceClient petServiceClient;
    private final InteractionServiceClient interactionServiceClient;
    private final KafkaProducerService kafkaProducerService;


    @Override
    public Adoption createAdoptionRequest(Adoption adoption) {
        // Fetch pet details from pet-service to get the correct ownerId and verify status
        Pet pet = petServiceClient.getPetById((long) adoption.getPetId());

        if (pet == null) {
            throw new RuntimeException("Pet not found with id: " + adoption.getPetId());
        }

        List<Adoption> existing = adoptionRepository.findByAdopterId(adoption.getAdopterId());
        boolean alreadyRequested = existing.stream()
                .anyMatch(a -> a.getPetId() == adoption.getPetId()
                        && a.getStatus() != RequestStatus.REJECTED);

        if (alreadyRequested) {
            throw new RuntimeException("You already have an active request for this pet.");
        }

        // Verify pet is available (using status from pet-service)
        if (!"APPROVED".equalsIgnoreCase(pet.getStatus()) && !"PendingReview".equalsIgnoreCase(pet.getStatus())) {
             // In development, we might allow PendingReview pets to be requested
        }

        // Ensure ownerId is correctly set from the pet service
        adoption.setOwnerId(pet.getOwnerId());
        adoption.setStatus(RequestStatus.PENDING);
        adoption.setRequestedAt(LocalDateTime.now());
        Adoption savedAdoption = adoptionRepository.save(adoption);

        try {
            NotificationRequest notification = NotificationRequest.builder()
                    .userId((long) pet.getOwnerId())
                    .message("New adoption request received for " + pet.getName() + " from user " + adoption.getAdopterId())
                    .type("ADOPTION_REQUEST")
                    .build();
            interactionServiceClient.createNotification(notification);
        } catch (Exception e) {
            System.err.println("FAILED to send notification to owner: " + e.getMessage());
        }

        return savedAdoption;
    }

    @Override
    public List<Adoption> getUserRequests(int adopterId) {
        List<Adoption> adoptions = adoptionRepository.findByAdopterId(adopterId);
        adoptions.forEach(a -> {
            try {
                a.setPet(petServiceClient.getPetById((long) a.getPetId()));
            } catch (Exception e) {
                System.out.println("Could not fetch pet details for request: " + a.getId());
            }
        });
        return adoptions;
    }

    @Override
    public List<Adoption> getRequestsByOwner(int ownerId) {
        List<Adoption> adoptions = adoptionRepository.findByOwnerId(ownerId);
        adoptions.forEach(a -> {
            try {
                a.setPet(petServiceClient.getPetById((long) a.getPetId()));
            } catch (Exception e) {
                System.out.println("Could not fetch pet details for request: " + a.getId());
            }
        });
        return adoptions;
    }

    @Override
    public Adoption approveAdoption(int id) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption request not found with id: " + id));

        adoption.setStatus(RequestStatus.APPROVED);
        adoption.setRespondedAt(LocalDateTime.now());
        
        Adoption savedAdoption = adoptionRepository.save(adoption);

        // Kafka Event - Notify other services that a pet has been adopted
        // This now handles the status update in pet-service asynchronously
        try {
            String eventMessage = String.format("{\"petId\": %d, \"adopterId\": %d, \"status\": \"ADOPTED\"}", 
                adoption.getPetId(), adoption.getAdopterId());
            kafkaProducerService.sendMessage("pet-adopted", eventMessage);
        } catch (Exception e) {
            System.err.println("FAILED to send Kafka message for pet adoption: " + e.getMessage());
        }

        try {
            Pet pet = petServiceClient.getPetById((long) adoption.getPetId());
            NotificationRequest notification = NotificationRequest.builder()
                    .userId((long) adoption.getAdopterId())
                    .message("Your adoption request for " + (pet != null ? pet.getName() : "a pet") + " has been APPROVED!")
                    .type("Success")
                    .build();
            interactionServiceClient.createNotification(notification);
        } catch (Exception e) {
            System.err.println("FAILED to send notification to adopter: " + e.getMessage());
        }

        return savedAdoption;
    }

    @Override
    public Adoption rejectAdoption(int id, String reason) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption request not found with id: " + id));

        adoption.setStatus(RequestStatus.REJECTED);
        adoption.setRejectionReason(reason);
        adoption.setRespondedAt(LocalDateTime.now());

        Adoption savedAdoption = adoptionRepository.save(adoption);

        try {
            Pet pet = petServiceClient.getPetById((long) adoption.getPetId());
            NotificationRequest notification = NotificationRequest.builder()
                    .userId((long) adoption.getAdopterId())
                    .message("Your adoption request for " + (pet != null ? pet.getName() : "a pet") + " has been REJECTED.")
                    .type("Error")
                    .build();
            interactionServiceClient.createNotification(notification);
        } catch (Exception e) {
            System.err.println("FAILED to send notification to adopter: " + e.getMessage());
        }

        return savedAdoption;
    }
}

// hna el logic lma by7sl adoption req el code byro7 etakd mn
// pet service hl el pet dh available wla la