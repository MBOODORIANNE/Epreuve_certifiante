package com.ecomove.ecoMove.controller;

import com.ecomove.ecoMove.dto.request.TripRequest;
import com.ecomove.ecoMove.dto.response.TripResponse;
import com.ecomove.ecoMove.dto.response.UserResponse;
import com.ecomove.ecoMove.service.TripService;
import com.ecomove.ecoMove.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des trajets de covoiturage.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@RestController
@RequestMapping("/api/trips")
@Tag(name = "Trajets", description = "Gestion des trajets de covoiturage")
@SecurityRequirement(name = "bearerAuth")
public class TripController {

    private final TripService tripService;
    private final UserService userService;

    public TripController(TripService tripService, UserService userService) {
        this.tripService = tripService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau trajet")
    public ResponseEntity<TripResponse> createTrip(Authentication authentication,
                                                    @Valid @RequestBody TripRequest request) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return new ResponseEntity<>(tripService.createTrip(user.getId(), request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un trajet par ID")
    public ResponseEntity<TripResponse> getTripById(@PathVariable Long id) {
        return ResponseEntity.ok(tripService.getTripById(id));
    }

    @GetMapping
    @Operation(summary = "Lister tous les trajets")
    public ResponseEntity<List<TripResponse>> getAllTrips() {
        return ResponseEntity.ok(tripService.getAllTrips());
    }

    @GetMapping("/my-trips")
    @Operation(summary = "Mes trajets en tant que conducteur")
    public ResponseEntity<List<TripResponse>> getMyTrips(Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(tripService.getTripsByDriver(user.getId()));
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des trajets par proximité géographique",
            description = "Algorithme de matching basé sur la proximité GPS du départ et de l'arrivée")
    public ResponseEntity<List<TripResponse>> searchTrips(
            @RequestParam Double departureLat,
            @RequestParam Double departureLng,
            @RequestParam Double arrivalLat,
            @RequestParam Double arrivalLng,
            @RequestParam(defaultValue = "10") Double radiusKm,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureStart,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime departureEnd) {
        return ResponseEntity.ok(tripService.searchTrips(departureLat, departureLng, arrivalLat, arrivalLng,
                radiusKm, departureStart, departureEnd));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un trajet")
    public ResponseEntity<TripResponse> updateTrip(@PathVariable Long id,
                                                    Authentication authentication,
                                                    @Valid @RequestBody TripRequest request) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(tripService.updateTrip(id, user.getId(), request));
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Annuler un trajet")
    public ResponseEntity<Void> cancelTrip(@PathVariable Long id, Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        tripService.cancelTrip(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/start")
    @Operation(summary = "Démarrer un trajet")
    public ResponseEntity<Void> startTrip(@PathVariable Long id, Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        tripService.startTrip(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/complete")
    @Operation(summary = "Marquer un trajet comme complété")
    public ResponseEntity<Void> completeTrip(@PathVariable Long id, Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        tripService.completeTrip(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
