package com.petadopt.adoptionservice.services;

import com.petadopt.adoptionservice.models.Adoption;

import java.util.List;

public interface AdoptionService {

    Adoption createAdoptionRequest(Adoption adoption);
    List<Adoption> getUserRequests(int adopterId);
    Adoption approveAdoption(int id);
    Adoption rejectAdoption(int id, String reason);
}
