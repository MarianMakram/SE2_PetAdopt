package com.petadopt.authservice.models.dto;

import com.petadopt.authservice.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String city;
    private String country;
    private Role role;
    private com.petadopt.authservice.models.enums.Status accountStatus;
}
