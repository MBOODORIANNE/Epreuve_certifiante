package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.request.BookingRequest;
import com.ecomove.ecoMove.dto.response.BookingResponse;
import com.ecomove.ecoMove.entity.Booking;
import com.ecomove.ecoMove.entity.Trip;
import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.entity.enums.BookingStatus;
import com.ecomove.ecoMove.entity.enums.Role;
import com.ecomove.ecoMove.entity.enums.TripStatus;
import com.ecomove.ecoMove.exception.BadRequestException;
import com.ecomove.ecoMove.repository.BookingRepository;
import com.ecomove.ecoMove.repository.TripRepository;
import com.ecomove.ecoMove.repository.UserRepository;
import com.ecomove.ecoMove.service.impl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du service de gestion des réservations.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du BookingService")
class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BookingServiceImpl bookingService;

    private User driver;
    private User passenger;
    private Trip trip;
    private Booking booking;

    @BeforeEach
    void setUp() {
        driver = new User("driver@ecomove.com", "pass", "Jean", "Driver", Role.EMPLOYEE);
        driver.setId(1L);
        driver.setCreatedAt(LocalDateTime.now());

        passenger = new User("passenger@ecomove.com", "pass", "Alice", "Passenger", Role.EMPLOYEE);
        passenger.setId(2L);
        passenger.setCreatedAt(LocalDateTime.now());

        trip = new Trip();
        trip.setId(1L);
        trip.setDriver(driver);
        trip.setDepartureAddress("Départ");
        trip.setArrivalAddress("Arrivée");
        trip.setDepartureLat(48.85);
        trip.setDepartureLng(2.35);
        trip.setArrivalLat(45.76);
        trip.setArrivalLng(4.83);
        trip.setAvailableSeats(3);
        trip.setStatus(TripStatus.PLANNED);
        trip.setBookings(new ArrayList<>());
        trip.setCreatedAt(LocalDateTime.now());

        booking = new Booking(trip, passenger, 1);
        booking.setId(1L);
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Créer une réservation - Succès")
    void createBooking_Success() {
        BookingRequest request = new BookingRequest(1L, 1);
        when(userRepository.findById(2L)).thenReturn(Optional.of(passenger));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(bookingRepository.existsByTripIdAndPassengerId(1L, 2L)).thenReturn(false);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        BookingResponse result = bookingService.createBooking(2L, request);

        assertNotNull(result);
        assertEquals("PENDING", result.getStatus());
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Créer une réservation - Le conducteur ne peut pas réserver son propre trajet")
    void createBooking_DriverCannotBookOwnTrip() {
        BookingRequest request = new BookingRequest(1L, 1);
        when(userRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThrows(BadRequestException.class, () -> bookingService.createBooking(1L, request));
    }

    @Test
    @DisplayName("Créer une réservation - Double réservation interdite")
    void createBooking_DuplicateBooking() {
        BookingRequest request = new BookingRequest(1L, 1);
        when(userRepository.findById(2L)).thenReturn(Optional.of(passenger));
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(bookingRepository.existsByTripIdAndPassengerId(1L, 2L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> bookingService.createBooking(2L, request));
    }

    @Test
    @DisplayName("Confirmer une réservation - Succès")
    void confirmBooking_Success() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        assertDoesNotThrow(() -> bookingService.confirmBooking(1L, 1L));
        verify(bookingRepository, times(1)).save(any(Booking.class));
    }

    @Test
    @DisplayName("Annuler une réservation - Succès par le passager")
    void cancelBooking_ByPassenger() {
        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);

        assertDoesNotThrow(() -> bookingService.cancelBooking(1L, 2L));
    }
}
