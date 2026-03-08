package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.request.BookingRequest;
import com.ecomove.ecoMove.dto.response.BookingResponse;
import java.util.List;

/**
 * Interface du service de gestion des réservations de covoiturage.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public interface BookingService {

    BookingResponse createBooking(Long passengerId, BookingRequest request);

    BookingResponse getBookingById(Long id);

    List<BookingResponse> getBookingsByPassenger(Long passengerId);

    List<BookingResponse> getBookingsByTrip(Long tripId);

    void confirmBooking(Long bookingId, Long driverId);

    void cancelBooking(Long bookingId, Long userId);

    void completeBooking(Long bookingId);
}
