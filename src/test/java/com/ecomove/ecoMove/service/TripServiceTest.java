package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.request.TripRequest;
import com.ecomove.ecoMove.dto.response.TripResponse;
import com.ecomove.ecoMove.entity.Trip;
import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.entity.Vehicle;
import com.ecomove.ecoMove.entity.enums.Role;
import com.ecomove.ecoMove.entity.enums.TripStatus;
import com.ecomove.ecoMove.entity.enums.VehicleType;
import com.ecomove.ecoMove.exception.BadRequestException;
import com.ecomove.ecoMove.exception.ResourceNotFoundException;
import com.ecomove.ecoMove.exception.UnauthorizedException;
import com.ecomove.ecoMove.repository.TripRepository;
import com.ecomove.ecoMove.repository.UserRepository;
import com.ecomove.ecoMove.repository.VehicleRepository;
import com.ecomove.ecoMove.service.impl.TripServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du service de gestion des trajets.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du TripService")
class TripServiceTest {

    @Mock
    private TripRepository tripRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private TripServiceImpl tripService;

    private User driver;
    private Vehicle vehicle;
    private Trip trip;
    private TripRequest tripRequest;

    @BeforeEach
    void setUp() {
        driver = new User("driver@ecomove.com", "pass", "Jean", "Dupont", Role.EMPLOYEE);
        driver.setId(1L);
        driver.setCreatedAt(LocalDateTime.now());

        vehicle = new Vehicle("Renault", "Clio", "AB-123-CD", 4, VehicleType.SEDAN, driver);
        vehicle.setId(1L);
        vehicle.setColor("Bleu");
        vehicle.setCreatedAt(LocalDateTime.now());

        trip = new Trip();
        trip.setId(1L);
        trip.setDriver(driver);
        trip.setDepartureAddress("10 Rue de Paris");
        trip.setDepartureLat(48.8566);
        trip.setDepartureLng(2.3522);
        trip.setArrivalAddress("20 Avenue de Lyon");
        trip.setArrivalLat(45.7640);
        trip.setArrivalLng(4.8357);
        trip.setDepartureTime(LocalDateTime.now().plusDays(1));
        trip.setAvailableSeats(3);
        trip.setPricePerSeat(new BigDecimal("5.00"));
        trip.setStatus(TripStatus.PLANNED);
        trip.setVehicle(vehicle);
        trip.setDistanceKm(462.0);
        trip.setEstimatedCO2SavedKg(55.44);
        trip.setBookings(new ArrayList<>());
        trip.setCreatedAt(LocalDateTime.now());

        tripRequest = new TripRequest();
        tripRequest.setDepartureAddress("10 Rue de Paris");
        tripRequest.setDepartureLat(48.8566);
        tripRequest.setDepartureLng(2.3522);
        tripRequest.setArrivalAddress("20 Avenue de Lyon");
        tripRequest.setArrivalLat(45.7640);
        tripRequest.setArrivalLng(4.8357);
        tripRequest.setDepartureTime(LocalDateTime.now().plusDays(1));
        tripRequest.setAvailableSeats(3);
        tripRequest.setPricePerSeat(new BigDecimal("5.00"));
        tripRequest.setVehicleId(1L);
    }

    @Test
    @DisplayName("Créer un trajet - Succès")
    void createTrip_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(driver));
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        TripResponse result = tripService.createTrip(1L, tripRequest);

        assertNotNull(result);
        assertEquals("10 Rue de Paris", result.getDepartureAddress());
        assertEquals("20 Avenue de Lyon", result.getArrivalAddress());
        assertEquals(3, result.getAvailableSeats());
        verify(tripRepository, times(1)).save(any(Trip.class));
    }

    @Test
    @DisplayName("Créer un trajet - Conducteur non trouvé")
    void createTrip_DriverNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> tripService.createTrip(999L, tripRequest));
    }

    @Test
    @DisplayName("Récupérer un trajet par ID")
    void getTripById_Success() {
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        TripResponse result = tripService.getTripById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(tripRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Lister tous les trajets")
    void getAllTrips_Success() {
        when(tripRepository.findAll()).thenReturn(Arrays.asList(trip));

        List<TripResponse> results = tripService.getAllTrips();

        assertNotNull(results);
        assertEquals(1, results.size());
    }

    @Test
    @DisplayName("Annuler un trajet - Succès")
    void cancelTrip_Success() {
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        assertDoesNotThrow(() -> tripService.cancelTrip(1L, 1L));
        verify(tripRepository, times(1)).save(any(Trip.class));
    }

    @Test
    @DisplayName("Annuler un trajet - Non autorisé")
    void cancelTrip_Unauthorized() {
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThrows(UnauthorizedException.class, () -> tripService.cancelTrip(1L, 999L));
    }

    @Test
    @DisplayName("Démarrer un trajet - Succès")
    void startTrip_Success() {
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));
        when(tripRepository.save(any(Trip.class))).thenReturn(trip);

        assertDoesNotThrow(() -> tripService.startTrip(1L, 1L));
    }

    @Test
    @DisplayName("Compléter un trajet - Mauvais statut")
    void completeTrip_WrongStatus() {
        trip.setStatus(TripStatus.PLANNED);
        when(tripRepository.findById(1L)).thenReturn(Optional.of(trip));

        assertThrows(BadRequestException.class, () -> tripService.completeTrip(1L, 1L));
    }
}
