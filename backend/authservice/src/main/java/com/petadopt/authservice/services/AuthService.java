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
import lombok.extern.slf4j.Slf4j;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request){
        log.info("Registration attempt for email: {}", request.getEmail());
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
        log.info("Saving user to database...");
        userRepository.save(user);
        log.info("User saved successfully with ID: {}", user.getId());

        HashMap<String , Object> extraClaims = new HashMap<>();
        extraClaims.put("role",user.getRole().name());
        String jwtToken = jwtService.generateToken(user.getEmail(),extraClaims);

        return AuthResponse.builder().accessToken(jwtToken)
                .refreshToken("placeholder-refresh-token")   //nftkroooooooo
                .userId(user.getId()).email(user.getEmail()).firstName(user.getFirstName())
                .lastName(user.getLastName()).phone(user.getPhone()).city(user.getCity())
                .country(user.getCountry()).role(user.getRole()).accountStatus(user.getAccountStatus())
                .build();
    }
    public AuthResponse login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("User not found"));
        if (user.getAccountStatus() == Status.REJECTED) {
            throw new RuntimeException("this account is rejected");
        }
        if (user.getAccountStatus() == Status.PENDING) {
            throw new RuntimeException("Account is pending approval");
        }
        if (user.getAccountStatus() != Status.APPROVED) {
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
                .userId(user.getId()).email(user.getEmail()).firstName(user.getFirstName())
                .lastName(user.getLastName()).phone(user.getPhone()).city(user.getCity())
                .country(user.getCountry()).role(user.getRole()).accountStatus(user.getAccountStatus())
                .build();
    }

    public void logout(String refreshToken) {
        log.info("Logout attempt for token: {}", refreshToken);
        // In a real implementation, we would blacklist the token or remove it from a DB
    }

    public AuthResponse getMe(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()->new RuntimeException("User not found"));
        
        HashMap<String , Object> extraClaims = new HashMap<>();
        extraClaims.put("role",user.getRole().name());
        String jwtToken = jwtService.generateToken(user.getEmail(),extraClaims);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken("placeholder-refresh-token")
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .city(user.getCity())
                .country(user.getCountry())
                .role(user.getRole())
                .accountStatus(user.getAccountStatus())
                .build();
    }

    public java.util.List<AuthResponse> getUsersByStatus(String status) {
        java.util.List<User> users;
        if (status != null && !status.isEmpty() && !status.equalsIgnoreCase("ALL")) {
            Status accountStatus = Status.valueOf(status.toUpperCase());
            users = userRepository.findByAccountStatus(accountStatus);
        } else {
            users = userRepository.findAll();
        }
        return users.stream().map(user -> AuthResponse.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .city(user.getCity())
                .country(user.getCountry())
                .role(user.getRole())
                .accountStatus(user.getAccountStatus())
                .build()).collect(java.util.stream.Collectors.toList());
    }

    public void approveUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setAccountStatus(Status.APPROVED);
        userRepository.save(user);
    }

    public void rejectUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setAccountStatus(Status.REJECTED);
        userRepository.save(user);
    }
}
