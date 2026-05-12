package com.petadopt.petservice.services;

import com.petadopt.petservice.repositories.PetRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumerService {

    private final PetRepository petRepository;
    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper;

    @KafkaListener(topics = "pet-adopted", groupId = "pet-group")
    public void consumePetAdopted(String message) {
        log.info("Consumed message from pet-adopted topic: {}", message);
        try {
            com.fasterxml.jackson.databind.JsonNode node = objectMapper.readTree(message);
            Long petId = node.get("petId").asLong();
            
            petRepository.findById(petId).ifPresent(pet -> {
                pet.setStatus(com.petadopt.petservice.models.enums.PetStatus.ADOPTED);
                petRepository.save(pet);
                log.info("Successfully updated pet {} status to ADOPTED via Kafka", petId);
            });
        } catch (Exception e) {
            log.error("Failed to process pet-adopted message: {}", e.getMessage());
        }
    }
}
