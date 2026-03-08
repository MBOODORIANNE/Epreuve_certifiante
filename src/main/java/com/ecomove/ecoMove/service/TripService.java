package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.request.TripRequest;
import com.ecomove.ecoMove.dto.response.TripResponse;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Interface du service de gestion des trajets de covoiturage.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public interface TripService {

    TripResponse createTrip(Long driverId, TripRequest request);

    TripResponse getTripById(Long id);

    List<TripResponse> getAllTrips();

    List<TripResponse> getTripsByDriver(Long driverId);

    List<TripResponse> searchTrips(Double departureLat, Double departureLng,
                                    Double arrivalLat, Double arrivalLng,
                                    Double radiusKm, LocalDateTime departureStart,
                                    LocalDateTime departureEnd);

    TripResponse updateTrip(Long id, Long driverId, TripRequest request);

    void cancelTrip(Long id, Long userId);

    void startTrip(Long id, Long driverId);

    void completeTrip(Long id, Long driverId);
}
