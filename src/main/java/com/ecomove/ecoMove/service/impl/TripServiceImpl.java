package com.ecomove.ecoMove.service.impl;

import com.ecomove.ecoMove.dto.request.TripRequest;
import com.ecomove.ecoMove.dto.response.TripResponse;
import com.ecomove.ecoMove.entity.Trip;
import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.entity.Vehicle;
import com.ecomove.ecoMove.entity.enums.TripStatus;
import com.ecomove.ecoMove.entity.enums.VehicleType;
import com.ecomove.ecoMove.exception.BadRequestException;
import com.ecomove.ecoMove.exception.ResourceNotFoundException;
import com.ecomove.ecoMove.exception.UnauthorizedException;
import com.ecomove.ecoMove.mapper.EntityMapper;
import com.ecomove.ecoMove.repository.TripRepository;
import com.ecomove.ecoMove.repository.UserRepository;
import com.ecomove.ecoMove.repository.VehicleRepository;
import com.ecomove.ecoMove.service.TripService;
import com.ecomove.ecoMove.util.CarbonCalculator;
import com.ecomove.ecoMove.util.DistanceCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des trajets.
 * <p>
 * Inclut le calcul automatique de la distance et du CO2 économisé
 * lors de la création d'un trajet.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Service
public class TripServiceImpl implements TripService {

    private static final Logger logger = LoggerFactory.getLogger(TripServiceImpl.class);

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final VehicleRepository vehicleRepository;

    public TripServiceImpl(TripRepository tripRepository, UserRepository userRepository,
                           VehicleRepository vehicleRepository) {
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    @Transactional
    public TripResponse createTrip(Long driverId, TripRequest request) {
        User driver = userRepository.findById(driverId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", driverId));

        Trip trip = new Trip();
        trip.setDriver(driver);
        trip.setDepartureAddress(request.getDepartureAddress());
        trip.setDepartureLat(request.getDepartureLat());
        trip.setDepartureLng(request.getDepartureLng());
        trip.setArrivalAddress(request.getArrivalAddress());
        trip.setArrivalLat(request.getArrivalLat());
        trip.setArrivalLng(request.getArrivalLng());
        trip.setDepartureTime(request.getDepartureTime());
        trip.setEstimatedArrivalTime(request.getEstimatedArrivalTime());
        trip.setAvailableSeats(request.getAvailableSeats());
        trip.setPricePerSeat(request.getPricePerSeat());
        trip.setRecurring(request.isRecurring());
        trip.setDescription(request.getDescription());
        trip.setStatus(TripStatus.PLANNED);

        // Calculer la distance automatiquement
        double distance = DistanceCalculator.calculateDistanceKm(
                request.getDepartureLat(), request.getDepartureLng(),
                request.getArrivalLat(), request.getArrivalLng()
        );
        trip.setDistanceKm(distance);

        // Associer le véhicule si spécifié
        VehicleType vehicleType = VehicleType.SEDAN;
        if (request.getVehicleId() != null) {
            Vehicle vehicle = vehicleRepository.findById(request.getVehicleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Véhicule", "id", request.getVehicleId()));
            trip.setVehicle(vehicle);
            vehicleType = vehicle.getVehicleType();
        }

        // Calculer le CO2 économisé estimé par passager
        double co2Saved = CarbonCalculator.calculateCO2Saved(distance, 1, vehicleType);
        trip.setEstimatedCO2SavedKg(co2Saved);

        Trip savedTrip = tripRepository.save(trip);
        logger.info("Trajet créé avec succès - ID: {}, Conducteur: {}, Distance: {} km, CO2 économisé: {} kg",
                savedTrip.getId(), driver.getFullName(), distance, co2Saved);

        return EntityMapper.toTripResponse(savedTrip);
    }

    @Override
    @Transactional(readOnly = true)
    public TripResponse getTripById(Long id) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet", "id", id));
        return EntityMapper.toTripResponse(trip);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponse> getAllTrips() {
        return tripRepository.findAll().stream()
                .map(EntityMapper::toTripResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponse> getTripsByDriver(Long driverId) {
        return tripRepository.findByDriverId(driverId).stream()
                .map(EntityMapper::toTripResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TripResponse> searchTrips(Double departureLat, Double departureLng,
                                           Double arrivalLat, Double arrivalLng,
                                           Double radiusKm, LocalDateTime departureStart,
                                           LocalDateTime departureEnd) {
        if (radiusKm == null) radiusKm = 10.0;
        if (departureEnd == null) departureEnd = departureStart.plusDays(1);

        return tripRepository.searchTrips(departureLat, departureLng, arrivalLat, arrivalLng,
                        radiusKm, departureStart, departureEnd).stream()
                .map(EntityMapper::toTripResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TripResponse updateTrip(Long id, Long driverId, TripRequest request) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet", "id", id));

        if (!trip.getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à modifier ce trajet");
        }

        if (trip.getStatus() != TripStatus.PLANNED) {
            throw new BadRequestException("Impossible de modifier un trajet qui n'est pas à l'état PLANIFIÉ");
        }

        trip.setDepartureAddress(request.getDepartureAddress());
        trip.setDepartureLat(request.getDepartureLat());
        trip.setDepartureLng(request.getDepartureLng());
        trip.setArrivalAddress(request.getArrivalAddress());
        trip.setArrivalLat(request.getArrivalLat());
        trip.setArrivalLng(request.getArrivalLng());
        trip.setDepartureTime(request.getDepartureTime());
        trip.setEstimatedArrivalTime(request.getEstimatedArrivalTime());
        trip.setAvailableSeats(request.getAvailableSeats());
        trip.setPricePerSeat(request.getPricePerSeat());
        trip.setRecurring(request.isRecurring());
        trip.setDescription(request.getDescription());

        double distance = DistanceCalculator.calculateDistanceKm(
                request.getDepartureLat(), request.getDepartureLng(),
                request.getArrivalLat(), request.getArrivalLng()
        );
        trip.setDistanceKm(distance);

        Trip updatedTrip = tripRepository.save(trip);
        logger.info("Trajet mis à jour - ID: {}", updatedTrip.getId());

        return EntityMapper.toTripResponse(updatedTrip);
    }

    @Override
    @Transactional
    public void cancelTrip(Long id, Long userId) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet", "id", id));

        if (!trip.getDriver().getId().equals(userId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à annuler ce trajet");
        }

        trip.setStatus(TripStatus.CANCELLED);
        tripRepository.save(trip);
        logger.info("Trajet annulé - ID: {}", id);
    }

    @Override
    @Transactional
    public void startTrip(Long id, Long driverId) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet", "id", id));

        if (!trip.getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à démarrer ce trajet");
        }

        if (trip.getStatus() != TripStatus.PLANNED) {
            throw new BadRequestException("Le trajet doit être à l'état PLANIFIÉ pour être démarré");
        }

        trip.setStatus(TripStatus.IN_PROGRESS);
        tripRepository.save(trip);
        logger.info("Trajet démarré - ID: {}", id);
    }

    @Override
    @Transactional
    public void completeTrip(Long id, Long driverId) {
        Trip trip = tripRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trajet", "id", id));

        if (!trip.getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à compléter ce trajet");
        }

        if (trip.getStatus() != TripStatus.IN_PROGRESS) {
            throw new BadRequestException("Le trajet doit être EN COURS pour être complété");
        }

        trip.setStatus(TripStatus.COMPLETED);
        tripRepository.save(trip);
        logger.info("Trajet complété - ID: {}, CO2 économisé: {} kg", id, trip.getEstimatedCO2SavedKg());
    }
}
