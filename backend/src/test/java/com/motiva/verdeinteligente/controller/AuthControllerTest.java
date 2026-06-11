package com.motiva.verdeinteligente.controller;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.motiva.verdeinteligente.dto.AuthTokenResponse;
import com.motiva.verdeinteligente.dto.AuthenticatedUserResponse;
import com.motiva.verdeinteligente.dto.LoginRequest;
import com.motiva.verdeinteligente.service.AuthService;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    void shouldAuthenticateAndReturnToken() throws Exception {
        LoginRequest request = new LoginRequest("motiva.admin", "motiva@123");
        when(authService.login(request)).thenReturn(
            new AuthTokenResponse("token", "Bearer", LocalDateTime.of(2026, 6, 11, 22, 0), "motiva.admin", "Portfolio Operator", "MANAGER")
        );

        mockMvc.perform(post("/api/auth/login")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andExpect(jsonPath("$.username").value("motiva.admin"));
    }

    @Test
    void shouldReturnCurrentUser() throws Exception {
        when(authService.currentUser()).thenReturn(new AuthenticatedUserResponse("motiva.admin", "Portfolio Operator", "MANAGER"));

        mockMvc.perform(get("/api/auth/me"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fullName").value("Portfolio Operator"))
            .andExpect(jsonPath("$.role").value("MANAGER"));
    }
}
