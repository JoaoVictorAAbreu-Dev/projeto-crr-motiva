package com.motiva.verdeinteligente.service;

import com.motiva.verdeinteligente.dto.AuthTokenResponse;
import com.motiva.verdeinteligente.dto.AuthenticatedUserResponse;
import com.motiva.verdeinteligente.dto.LoginRequest;
import com.motiva.verdeinteligente.security.DemoUserDetailsService;
import com.motiva.verdeinteligente.security.JwtService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final DemoUserDetailsService demoUserDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
        DemoUserDetailsService demoUserDetailsService,
        PasswordEncoder passwordEncoder,
        JwtService jwtService
    ) {
        this.demoUserDetailsService = demoUserDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthTokenResponse login(LoginRequest request) {
        UserDetails userDetails = demoUserDetailsService.loadUserByUsername(request.username());
        if (!passwordEncoder.matches(request.password(), userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        AuthenticatedUserResponse currentUser = demoUserDetailsService.currentUser();
        String token = jwtService.generateToken(userDetails, currentUser.fullName(), currentUser.role());

        return new AuthTokenResponse(
            token,
            "Bearer",
            jwtService.extractExpiration(token),
            currentUser.username(),
            currentUser.fullName(),
            currentUser.role()
        );
    }

    public AuthenticatedUserResponse currentUser() {
        return demoUserDetailsService.currentUser();
    }
}
