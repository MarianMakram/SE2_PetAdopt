package com.petadopt.authservice.services;

import com.petadopt.authservice.models.User;
import com.petadopt.authservice.models.dto.AuthResponse;
import com.petadopt.authservice.models.dto.LoginRequest;
import com.petadopt.authservice.models.dto.RegisterRequest;
import com.petadopt.authservice.models.enums.Status;
import com.petadopt.authservice.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already in use");
        }

//        byte[] saltBytes = new byte[16];
//        new SecureRandom().nextBytes(saltBytes);
//        String salt= Base64.getEncoder().encodeToString(saltBytes);  //using BCrypt rather than make salt manually
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        Status initialStatus = request.getRole().name().equals("SHELTER") ? Status.PENDING : Status.APPROVED;
        User user = User.builder()
                .email(request.getEmail()).passwordHash(hashedPassword).firstName(request.getFirstName())
                .lastName(request.getLastName()).phone(request.getPhone()).city(request.getCity())
                .country(request.getCountry()).role(request.getRole()).accountStatus(initialStatus)
                .build();
        userRepository.save(user);

        HashMap<String , Object> extraClaims = new HashMap<>();
        extraClaims.put("role",user.getRole().name());
        String jwtToken = jwtService.generateToken(user.getEmail(),extraClaims);

        return AuthResponse.builder().accessToken(jwtToken)
                .refreshToken("placeholder-refresh-token")   //nftkroooooooo
                .userId(user.getId()).email(user.getEmail()).firstName(user.getFirstName())
                .lastName(user.getLastName()).role(user.getRole())
                .build();
    }
    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("User not found"));
        if (user.getAccountStatus() != Status.APPROVED){
            throw new RuntimeException("Account is not approved or is suspended");
        }
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }
        HashMap<String , Object> extraClaims = new HashMap<>();
        extraClaims.put("role",user.getRole().name());
        String jwtToken = jwtService.generateToken(user.getEmail(),extraClaims);

        return AuthResponse.builder().accessToken(jwtToken)
                .refreshToken("placeholder-refresh-token")    //nftkroooooooo
                .userId(user.getId()).email(user.getEmail()).firstName(user.getFirstName()).lastName(user.getLastName()).role(user.getRole())
                .build();
    }

    public AuthResponse getMe(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        return AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .build();
    }
}
