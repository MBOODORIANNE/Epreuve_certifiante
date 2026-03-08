package com.ecomove.ecoMove.controller;

import com.ecomove.ecoMove.dto.response.UserResponse;
import com.ecomove.ecoMove.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "Utilisateurs", description = "Gestion des profils utilisateurs")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Récupérer le profil connecté")
    public ResponseEntity<UserResponse> getCurrentUser(Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un utilisateur par ID")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ECOMOVE_ADMIN')")
    @Operation(summary = "Lister tous les utilisateurs (Admin)")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ECOMOVE_ADMIN')")
    @Operation(summary = "Lister les employés d'une entreprise")
    public ResponseEntity<List<UserResponse>> getUsersByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(userService.getUsersByCompany(companyId));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour le profil utilisateur")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String homeAddress,
            @RequestParam(required = false) Double homeLatitude,
            @RequestParam(required = false) Double homeLongitude) {
        return ResponseEntity.ok(userService.updateProfile(id, firstName, lastName, phone,
                homeAddress, homeLatitude, homeLongitude));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ECOMOVE_ADMIN')")
    @Operation(summary = "Désactiver un compte utilisateur (Admin)")
    public ResponseEntity<Void> deactivateUser(@PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ECOMOVE_ADMIN')")
    @Operation(summary = "Réactiver un compte utilisateur (Admin)")
    public ResponseEntity<Void> activateUser(@PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.noContent().build();
    }
}
