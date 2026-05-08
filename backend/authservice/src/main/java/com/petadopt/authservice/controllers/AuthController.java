package com.petadopt.authservice.controllers;

import com.petadopt.authservice.models.dto.AuthResponse;
import com.petadopt.authservice.models.dto.LoginRequest;
import com.petadopt.authservice.models.dto.RegisterRequest;
import com.petadopt.authservice.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    public final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request){
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public  ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        authService.logout(refreshToken);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me")
    public ResponseEntity<AuthResponse> getMe() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(authService.getMe(email));
    }

    @GetMapping("/users")
    public ResponseEntity<java.util.List<AuthResponse>> getUsersByStatus(@org.springframework.web.bind.annotation.RequestParam(required = false) String status) {
        return ResponseEntity.ok(authService.getUsersByStatus(status));
    }

    @org.springframework.web.bind.annotation.PutMapping("/users/{id}/approve")
    public ResponseEntity<Void> approveUser(@org.springframework.web.bind.annotation.PathVariable Integer id) {
        authService.approveUser(id);
        return ResponseEntity.ok().build();
    }

    @org.springframework.web.bind.annotation.PutMapping("/users/{id}/reject")
    public ResponseEntity<Void> rejectUser(@org.springframework.web.bind.annotation.PathVariable Integer id) {
        authService.rejectUser(id);
        return ResponseEntity.ok().build();
    }
}
