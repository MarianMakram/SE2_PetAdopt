package com.petadopt.interactionservice.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;
    private final com.petadopt.interactionservice.repositories.NotificationRepository notificationRepository;

    @KafkaListener(topics = "user-registered", groupId = "interaction-group")
    public void consumeUserRegistered(String message) {
        log.info("Consumed message from user-registered topic: {}", message);
        try {
            com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(message);
            Long userId = node.get("userId").asLong();
            String email = node.get("email").asText();
            
            com.petadopt.interactionservice.models.Notification notification = com.petadopt.interactionservice.models.Notification.builder()
                    .userId(userId)
                    .title("Welcome to PetAdopt!")
                    .message("Hello " + email + ", thank you for joining our community!")
                    .type("WELCOME")
                    .build();
            notificationRepository.save(notification);
            
            log.info("Saved registration notification for User ID: {}", userId);
        } catch (Exception e) {
            log.error("Error parsing user-registered event: {}", e.getMessage());
        }
    }

    @KafkaListener(topics = "pet-adopted", groupId = "interaction-group")
    public void consumePetAdopted(String message) {
        log.info("Consumed message from pet-adopted topic: {}", message);
        try {
            com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(message);
            Long petId = node.get("petId").asLong();
            Long adopterId = node.get("adopterId").asLong();

            com.petadopt.interactionservice.models.Notification notification = com.petadopt.interactionservice.models.Notification.builder()
                    .userId(adopterId)
                    .title("Congratulations!")
                    .message("Your request for pet #" + petId + " has been finalized.")
                    .type("ADOPTION_SUCCESS")
                    .relatedEntityId(petId.toString())
                    .relatedEntityType("PET")
                    .build();
            notificationRepository.save(notification);
            
            log.info("Saved adoption success notification for Adopter ID: {}", adopterId);
        } catch (Exception e) {
            log.error("Error parsing pet-adopted event: {}", e.getMessage());
        }
    }
}
