package com.ecomove.ecoMove.controller;

import com.ecomove.ecoMove.dto.request.LoginRequest;
import com.ecomove.ecoMove.dto.request.RegisterRequest;
import com.ecomove.ecoMove.dto.response.AuthResponse;
import com.ecomove.ecoMove.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur REST pour l'authentification des utilisateurs.
 * <p>
 * Fournit les endpoints d'inscription et de connexion.
 * Ces endpoints sont publics et ne nécessitent pas d'authentification.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentification", description = "Endpoints d'inscription et de connexion")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Inscrit un nouvel utilisateur sur la plateforme.
     *
     * @param request les données d'inscription
     * @return la réponse d'authentification avec le token JWT
     */
    @PostMapping("/register")
    @Operation(summary = "Inscription d'un nouvel utilisateur",
            description = "Crée un compte utilisateur et retourne un token JWT d'authentification")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        logger.info("Requête d'inscription reçue pour : {}", request.getEmail());
        AuthResponse response = authService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Authentifie un utilisateur existant.
     *
     * @param request les identifiants de connexion
     * @return la réponse d'authentification avec le token JWT
     */
    @PostMapping("/login")
    @Operation(summary = "Connexion d'un utilisateur",
            description = "Authentifie un utilisateur et retourne un token JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        logger.info("Requête de connexion reçue pour : {}", request.getEmail());
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
