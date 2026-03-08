package com.ecomove.ecoMove.controller;

import com.ecomove.ecoMove.dto.request.LoginRequest;
import com.ecomove.ecoMove.dto.request.RegisterRequest;
import com.ecomove.ecoMove.dto.response.AuthResponse;
import com.ecomove.ecoMove.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests d'intégration du contrôleur d'authentification.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("Tests du AuthController")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("Inscription - Succès")
    void register_Success() throws Exception {
        RegisterRequest request = new RegisterRequest("jarle@ecomove.com", "Password123", "Jarle", "MBONGO");
        AuthResponse response = new AuthResponse("jwt-token-123", 1L, "jarle@ecomove.com", "Jarle MBONGO", "EMPLOYEE");

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.email").value("jarle@ecomove.com"))
                .andExpect(jsonPath("$.role").value("EMPLOYEE"));
    }

    @Test
    @DisplayName("Inscription - Email invalide")
    void register_InvalidEmail() throws Exception {
        RegisterRequest request = new RegisterRequest("invalid-email", "Password123", "Jarle", "MBONGO");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Inscription - Mot de passe trop court")
    void register_ShortPassword() throws Exception {
        RegisterRequest request = new RegisterRequest("jarle@ecomove.com", "short", "Jarle", "MBONGO");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Connexion - Succès")
    void login_Success() throws Exception {
        LoginRequest request = new LoginRequest("jarle@ecomove.com", "Password123");
        AuthResponse response = new AuthResponse("jwt-token-123", 1L, "jarle@ecomove.com", "Jarle MBONGO", "EMPLOYEE");

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-123"))
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    @DisplayName("Connexion - Champs manquants")
    void login_MissingFields() throws Exception {
        LoginRequest request = new LoginRequest();

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
