package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.request.LoginRequest;
import com.ecomove.ecoMove.dto.request.RegisterRequest;
import com.ecomove.ecoMove.dto.response.AuthResponse;

/**
 * Interface du service d'authentification.
 * <p>
 * Gère l'inscription, la connexion et la génération de tokens JWT.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public interface AuthService {

    /**
     * Inscrit un nouvel utilisateur sur la plateforme.
     *
     * @param request les données d'inscription
     * @return la réponse d'authentification avec le token JWT
     */
    AuthResponse register(RegisterRequest request);

    /**
     * Authentifie un utilisateur existant.
     *
     * @param request les identifiants de connexion
     * @return la réponse d'authentification avec le token JWT
     */
    AuthResponse login(LoginRequest request);
}
