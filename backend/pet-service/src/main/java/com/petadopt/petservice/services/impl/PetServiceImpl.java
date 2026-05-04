package com.petadopt.petservice.services.impl;

import com.petadopt.petservice.models.Pet;
import com.petadopt.petservice.models.enums.PetStatus;
import com.petadopt.petservice.models.enums.Species;
import com.petadopt.petservice.repositories.PetRepository;
import com.petadopt.petservice.services.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Override
    public Pet addPet(Pet pet) {
        return petRepository.save(pet);
    }

    @Override
    public Pet updatePet(Long id, Pet pet) {
        Pet existingPet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + id));
        
        existingPet.setName(pet.getName());
        existingPet.setBreed(pet.getBreed());
        existingPet.setSpecies(pet.getSpecies());
        existingPet.setAge(pet.getAge());
        existingPet.setAgeUnit(pet.getAgeUnit());
        existingPet.setGender(pet.getGender());
        existingPet.setStatus(pet.getStatus());
        existingPet.setDescription(pet.getDescription());
        existingPet.setHealthStatus(pet.getHealthStatus());
        existingPet.setLocation(pet.getLocation());
        existingPet.setImageUrls(pet.getImageUrls());
        
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
}
