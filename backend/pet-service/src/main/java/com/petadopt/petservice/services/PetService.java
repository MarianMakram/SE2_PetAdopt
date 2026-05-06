package com.petadopt.petservice.services;

import com.petadopt.petservice.models.Pet;
import com.petadopt.petservice.models.enums.PetStatus;
import com.petadopt.petservice.models.enums.Species;

import java.util.List;

public interface PetService {
    Pet addPet(Pet pet);
    Pet updatePet(Long id, Pet pet);
    void deletePet(Long id);
    Pet getPetById(Long id);
    List<Pet> getAllPets();
    List<Pet> getPetsByStatus(PetStatus status);
    List<Pet> getPetsBySpeciesAndStatus(Species species, PetStatus status);
    List<Pet> getPetsByBreed(String breed);
    List<Pet> getPetsByOwner(Long ownerId);
}
