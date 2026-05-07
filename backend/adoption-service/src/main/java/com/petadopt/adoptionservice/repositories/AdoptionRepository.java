package com.petadopt.adoptionservice.repositories;

import com.petadopt.adoptionservice.models.Adoption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdoptionRepository extends JpaRepository<Adoption, Integer> {
    List<Adoption> findByAdopterId(int adopterId);
    List<Adoption> findByOwnerId(int ownerId);
}
