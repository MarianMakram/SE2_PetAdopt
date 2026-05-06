package com.petadopt.petservice.controllers;

import com.petadopt.petservice.models.Pet;
import com.petadopt.petservice.models.enums.PetStatus;
import com.petadopt.petservice.models.enums.Species;
import com.petadopt.petservice.services.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets(
            @RequestParam(required = false) Species species,
            @RequestParam(required = false) PetStatus status,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Long ownerId) {
        
        System.out.println("DEBUG: getAllPets called with ownerId=" + ownerId + ", status=" + status);

        if (ownerId != null) {
            return ResponseEntity.ok(petService.getPetsByOwner(ownerId));
        } else if (breed != null) {
            return ResponseEntity.ok(petService.getPetsByBreed(breed));
        } else if (species != null && status != null) {
            return ResponseEntity.ok(petService.getPetsBySpeciesAndStatus(species, status));
        } else if (status != null) {
            return ResponseEntity.ok(petService.getPetsByStatus(status));
        }
        
        // Default behavior: return only APPROVED pets for public view
        return ResponseEntity.ok(petService.getPetsByStatus(PetStatus.APPROVED));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        return ResponseEntity.ok(petService.getPetById(id));
    }

    @PostMapping
    public ResponseEntity<Pet> addPet(@RequestBody Pet pet) {
        return new ResponseEntity<>(petService.addPet(pet), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet pet) {
        return ResponseEntity.ok(petService.updatePet(id, pet));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.noContent().build();
    }
}
