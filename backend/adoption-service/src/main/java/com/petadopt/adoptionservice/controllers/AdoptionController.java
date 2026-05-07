package com.petadopt.adoptionservice.controllers;

import com.petadopt.adoptionservice.models.Adoption;
import com.petadopt.adoptionservice.services.AdoptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/adoption-requests")
@RequiredArgsConstructor
public class AdoptionController {
    private final AdoptionService adoptionService;



    @PostMapping
    public ResponseEntity<Adoption> createAdoptionRequest(@RequestBody Adoption adoption) {
        return new ResponseEntity<>(adoptionService.createAdoptionRequest(adoption), HttpStatus.CREATED);
    }

    @GetMapping("/user/{adopterId}")
    public ResponseEntity<List<Adoption>> getUserRequests(@PathVariable int adopterId) {
        return ResponseEntity.ok(adoptionService.getUserRequests(adopterId));
    }
    
    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Adoption>> getOwnerRequests(@PathVariable int ownerId) {
        return ResponseEntity.ok(adoptionService.getRequestsByOwner(ownerId));
    }


    @PutMapping("/{id}/approve")
    public ResponseEntity<Adoption> approveAdoption(@PathVariable int id) {
        return ResponseEntity.ok(adoptionService.approveAdoption(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<Adoption> rejectAdoption(
            @PathVariable int id, 
            @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(adoptionService.rejectAdoption(id, reason));
    }
}
