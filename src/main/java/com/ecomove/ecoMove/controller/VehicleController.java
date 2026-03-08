package com.ecomove.ecoMove.controller;

import com.ecomove.ecoMove.dto.request.VehicleRequest;
import com.ecomove.ecoMove.dto.response.UserResponse;
import com.ecomove.ecoMove.dto.response.VehicleResponse;
import com.ecomove.ecoMove.service.UserService;
import com.ecomove.ecoMove.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des véhicules.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@RestController
@RequestMapping("/api/vehicles")
@Tag(name = "Véhicules", description = "Gestion des véhicules des conducteurs")
@SecurityRequirement(name = "bearerAuth")
public class VehicleController {

    private final VehicleService vehicleService;
    private final UserService userService;

    public VehicleController(VehicleService vehicleService, UserService userService) {
        this.vehicleService = vehicleService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un véhicule")
    public ResponseEntity<VehicleResponse> addVehicle(Authentication authentication,
                                                       @Valid @RequestBody VehicleRequest request) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return new ResponseEntity<>(vehicleService.addVehicle(user.getId(), request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un véhicule par ID")
    public ResponseEntity<VehicleResponse> getVehicleById(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleService.getVehicleById(id));
    }

    @GetMapping("/my-vehicles")
    @Operation(summary = "Mes véhicules")
    public ResponseEntity<List<VehicleResponse>> getMyVehicles(Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(vehicleService.getVehiclesByOwner(user.getId()));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un véhicule")
    public ResponseEntity<VehicleResponse> updateVehicle(@PathVariable Long id,
                                                          Authentication authentication,
                                                          @Valid @RequestBody VehicleRequest request) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(vehicleService.updateVehicle(id, user.getId(), request));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un véhicule")
    public ResponseEntity<Void> deleteVehicle(@PathVariable Long id, Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        vehicleService.deleteVehicle(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
