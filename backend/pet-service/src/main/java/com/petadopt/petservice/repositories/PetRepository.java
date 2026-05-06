package com.petadopt.petservice.repositories;

import com.petadopt.petservice.models.Pet;
import com.petadopt.petservice.models.enums.PetStatus;
import com.petadopt.petservice.models.enums.Species;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findBySpeciesAndStatus(Species species, PetStatus status);
    List<Pet> findByStatus(PetStatus status);
    List<Pet> findByBreedContainingIgnoreCase(String breed);
    List<Pet> findByOwnerId(Long ownerId);
}
