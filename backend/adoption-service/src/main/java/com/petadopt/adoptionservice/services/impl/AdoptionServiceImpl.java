package com.petadopt.adoptionservice.services.impl;

import com.petadopt.adoptionservice.client.PetServiceClient;
import com.petadopt.adoptionservice.models.Adoption;
import com.petadopt.adoptionservice.models.Pet;
import com.petadopt.adoptionservice.models.enums.RequestStatus;
import com.petadopt.adoptionservice.repositories.AdoptionRepository;
import com.petadopt.adoptionservice.services.AdoptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdoptionServiceImpl implements AdoptionService {

    private final AdoptionRepository adoptionRepository;
    private final PetServiceClient petServiceClient;


    @Override
    public Adoption createAdoptionRequest(Adoption adoption) {

        // MOCKED
        Pet pet = new Pet();
        pet.setId(adoption.getPetId());
        pet.setStatus("APPROVED");

        if (pet == null || !"APPROVED".equalsIgnoreCase(pet.getStatus())) {
            throw new RuntimeException("Pet is not available for adoption");
        }

        adoption.setStatus(RequestStatus.PENDING);
        return adoptionRepository.save(adoption);
    }

    @Override
    public List<Adoption> getUserRequests(int adopterId) {
        return adoptionRepository.findByAdopterId(adopterId);
    }

    @Override
    public Adoption approveAdoption(int id) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption request not found with id: " + id));

        adoption.setStatus(RequestStatus.APPROVED);
        adoption.setRespondedAt(LocalDateTime.now());
        
        Adoption savedAdoption = adoptionRepository.save(adoption);

        // MOCKED: Bypassing pet service update
        System.out.println("MOCK: Pet status updated to ADOPTED for petId: " + adoption.getPetId());

        return savedAdoption;
    }

    @Override
    public Adoption rejectAdoption(int id, String reason) {
        Adoption adoption = adoptionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adoption request not found with id: " + id));

        adoption.setStatus(RequestStatus.REJECTED);
        adoption.setRejectionReason(reason);
        adoption.setRespondedAt(LocalDateTime.now());

        return adoptionRepository.save(adoption);
    }
}

// hna el logic lma by7sl adoption req el code byro7 etakd mn
// pet service hl el pet dh available wla la