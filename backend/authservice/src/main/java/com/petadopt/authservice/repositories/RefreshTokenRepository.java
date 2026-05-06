package com.petadopt.authservice.repositories;

import com.petadopt.authservice.models.RefreshToken;
import com.petadopt.authservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);
    void deleteByUser(User user); // lma el user y logout
}
