package com.petadopt.authservice.repositories;

import com.petadopt.authservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository  extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);  //optional :- 3l4an may yrg3 user ya null
    boolean existsByEmail(String email);
    
    java.util.List<User> findByAccountStatus(com.petadopt.authservice.models.enums.Status accountStatus);
    java.util.List<User> findByRole(com.petadopt.authservice.models.enums.Role role);
}
