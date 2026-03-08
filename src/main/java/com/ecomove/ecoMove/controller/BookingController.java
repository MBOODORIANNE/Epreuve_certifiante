package com.ecomove.ecoMove.controller;

import com.ecomove.ecoMove.dto.request.BookingRequest;
import com.ecomove.ecoMove.dto.response.BookingResponse;
import com.ecomove.ecoMove.dto.response.UserResponse;
import com.ecomove.ecoMove.service.BookingService;
import com.ecomove.ecoMove.service.UserService;
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
 * Contrôleur REST pour la gestion des réservations de covoiturage.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@RestController
@RequestMapping("/api/bookings")
@Tag(name = "Réservations", description = "Gestion des réservations de covoiturage")
@SecurityRequirement(name = "bearerAuth")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Créer une réservation")
    public ResponseEntity<BookingResponse> createBooking(Authentication authentication,
                                                          @Valid @RequestBody BookingRequest request) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return new ResponseEntity<>(bookingService.createBooking(user.getId(), request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une réservation par ID")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long id) {
        return ResponseEntity.ok(bookingService.getBookingById(id));
    }

    @GetMapping("/my-bookings")
    @Operation(summary = "Mes réservations en tant que passager")
    public ResponseEntity<List<BookingResponse>> getMyBookings(Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(bookingService.getBookingsByPassenger(user.getId()));
    }

    @GetMapping("/trip/{tripId}")
    @Operation(summary = "Réservations pour un trajet donné")
    public ResponseEntity<List<BookingResponse>> getBookingsByTrip(@PathVariable Long tripId) {
        return ResponseEntity.ok(bookingService.getBookingsByTrip(tripId));
    }

    @PutMapping("/{id}/confirm")
    @Operation(summary = "Confirmer une réservation (conducteur)")
    public ResponseEntity<Void> confirmBooking(@PathVariable Long id, Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        bookingService.confirmBooking(id, user.getId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Annuler une réservation")
    public ResponseEntity<Void> cancelBooking(@PathVariable Long id, Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        bookingService.cancelBooking(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
