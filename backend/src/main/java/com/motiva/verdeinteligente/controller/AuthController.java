package com.motiva.verdeinteligente.controller;

import com.motiva.verdeinteligente.dto.AuthTokenResponse;
import com.motiva.verdeinteligente.dto.AuthenticatedUserResponse;
import com.motiva.verdeinteligente.dto.LoginRequest;
import com.motiva.verdeinteligente.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthTokenResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @GetMapping("/me")
    public AuthenticatedUserResponse me() {
        return authService.currentUser();
    }
}
