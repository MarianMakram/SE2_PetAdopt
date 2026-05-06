package com.petadopt.adoptionservice.client;

import com.petadopt.adoptionservice.models.Pet;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pet-service", url = "${pet.service.url:http://pet-service:8081}")
public interface PetServiceClient {

    @GetMapping("/api/pets/{id}")
    Pet getPetById(@PathVariable("id") Long id);

    @PutMapping("/api/pets/{id}")
    Pet updatePet(@PathVariable("id") Long id, @RequestBody Pet pet);
}
