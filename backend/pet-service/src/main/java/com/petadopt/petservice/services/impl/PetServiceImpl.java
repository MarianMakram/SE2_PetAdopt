package com.petadopt.petservice.services.impl;

import com.petadopt.petservice.models.Pet;
import com.petadopt.petservice.models.enums.PetStatus;
import com.petadopt.petservice.models.enums.Species;
import com.petadopt.petservice.repositories.PetRepository;
import com.petadopt.petservice.services.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.petadopt.petservice.client.InteractionServiceClient;
import com.petadopt.petservice.models.dto.NotificationRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final InteractionServiceClient interactionServiceClient;

    @Override
    public Pet addPet(Pet pet) {
        Pet savedPet = petRepository.save(pet);
        
        try {
            // Hardcode 9L as the admin id based on the seed
            NotificationRequest notification = NotificationRequest.builder()
                    .userId(9L)
                    .message("New pet creation request pending approval: " + savedPet.getName() + " (" + savedPet.getSpecies() + ")")
                    .type("PET_APPROVAL")
                    .build();
            interactionServiceClient.createNotification(notification);
        } catch (Exception e) {
            System.err.println("Failed to send notification to admin: " + e.getMessage());
        }

        return savedPet;
    }

    @Override
    public Pet updatePet(Long id, Pet pet) {
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));
        
        if (pet.getName() != null) existingPet.setName(pet.getName());
        if (pet.getBreed() != null) existingPet.setBreed(pet.getBreed());
        if (pet.getSpecies() != null) existingPet.setSpecies(pet.getSpecies());
        if (pet.getAge() != null) existingPet.setAge(pet.getAge());
        if (pet.getAgeUnit() != null) existingPet.setAgeUnit(pet.getAgeUnit());
        if (pet.getGender() != null) existingPet.setGender(pet.getGender());
        if (pet.getStatus() != null) existingPet.setStatus(pet.getStatus());
        if (pet.getDescription() != null) existingPet.setDescription(pet.getDescription());
        if (pet.getHealthStatus() != null) existingPet.setHealthStatus(pet.getHealthStatus());
        if (pet.getLocation() != null) existingPet.setLocation(pet.getLocation());
        if (pet.getImageUrls() != null) existingPet.setImageUrls(pet.getImageUrls());
        
        return petRepository.save(existingPet);
    }

    @Override
    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }

    @Override
    public Pet getPetById(Long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));
    }

    @Override
    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    @Override
    public List<Pet> getPetsByStatus(PetStatus status) {
        return petRepository.findByStatus(status);
    }

    @Override
    public List<Pet> getPetsBySpeciesAndStatus(Species species, PetStatus status) {
        return petRepository.findBySpeciesAndStatus(species, status);
    }

    @Override
    public List<Pet> getPetsByBreed(String breed) {
        return petRepository.findByBreedContainingIgnoreCase(breed);
    }

    @Override
    public List<Pet> getPetsByOwner(Long ownerId) {
        return petRepository.findByOwnerId(ownerId);
    }
}
