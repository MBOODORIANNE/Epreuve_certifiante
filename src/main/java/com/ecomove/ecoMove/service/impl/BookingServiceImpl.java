package com.ecomove.ecoMove.service.impl;

import com.ecomove.ecoMove.dto.request.BookingRequest;
import com.ecomove.ecoMove.dto.response.BookingResponse;
import com.ecomove.ecoMove.entity.Booking;
import com.ecomove.ecoMove.entity.Trip;
import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.entity.enums.BookingStatus;
import com.ecomove.ecoMove.entity.enums.TripStatus;
import com.ecomove.ecoMove.exception.BadRequestException;
import com.ecomove.ecoMove.exception.ResourceNotFoundException;
import com.ecomove.ecoMove.exception.UnauthorizedException;
import com.ecomove.ecoMove.mapper.EntityMapper;
import com.ecomove.ecoMove.repository.BookingRepository;
import com.ecomove.ecoMove.repository.TripRepository;
import com.ecomove.ecoMove.repository.UserRepository;
import com.ecomove.ecoMove.service.BookingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des réservations.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Service
public class BookingServiceImpl implements BookingService {

    private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, TripRepository tripRepository,
                              UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.tripRepository = tripRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(Long passengerId, BookingRequest request) {
        User passenger = userRepository.findById(passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", passengerId));

        Trip trip = tripRepository.findById(request.getTripId())
                .orElseThrow(() -> new ResourceNotFoundException("Trajet", "id", request.getTripId()));

        // Vérifications métier
        if (trip.getStatus() != TripStatus.PLANNED) {
            throw new BadRequestException("Impossible de réserver un trajet qui n'est pas à l'état PLANIFIÉ");
        }

        if (trip.getDriver().getId().equals(passengerId)) {
            throw new BadRequestException("Le conducteur ne peut pas réserver son propre trajet");
        }

        if (bookingRepository.existsByTripIdAndPassengerId(request.getTripId(), passengerId)) {
            throw new BadRequestException("Vous avez déjà une réservation pour ce trajet");
        }

        if (trip.getRemainingSeats() < request.getSeatsBooked()) {
            throw new BadRequestException("Nombre de places insuffisant. Places disponibles : " + trip.getRemainingSeats());
        }

        Booking booking = new Booking();
        booking.setTrip(trip);
        booking.setPassenger(passenger);
        booking.setSeatsBooked(request.getSeatsBooked());
        booking.setPickupAddress(request.getPickupAddress());
        booking.setPickupLatitude(request.getPickupLatitude());
        booking.setPickupLongitude(request.getPickupLongitude());
        booking.setStatus(BookingStatus.PENDING);

        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Réservation créée - ID: {}, Trajet: {}, Passager: {}",
                savedBooking.getId(), trip.getId(), passenger.getFullName());

        return EntityMapper.toBookingResponse(savedBooking);
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponse getBookingById(Long id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", "id", id));
        return EntityMapper.toBookingResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByPassenger(Long passengerId) {
        return bookingRepository.findByPassengerId(passengerId).stream()
                .map(EntityMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponse> getBookingsByTrip(Long tripId) {
        return bookingRepository.findByTripId(tripId).stream()
                .map(EntityMapper::toBookingResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void confirmBooking(Long bookingId, Long driverId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", "id", bookingId));

        if (!booking.getTrip().getDriver().getId().equals(driverId)) {
            throw new UnauthorizedException("Seul le conducteur peut confirmer cette réservation");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BadRequestException("La réservation doit être en attente pour être confirmée");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);
        logger.info("Réservation confirmée - ID: {}", bookingId);
    }

    @Override
    @Transactional
    public void cancelBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", "id", bookingId));

        boolean isPassenger = booking.getPassenger().getId().equals(userId);
        boolean isDriver = booking.getTrip().getDriver().getId().equals(userId);

        if (!isPassenger && !isDriver) {
            throw new UnauthorizedException("Vous n'êtes pas autorisé à annuler cette réservation");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        logger.info("Réservation annulée - ID: {}, Par: {}", bookingId, isPassenger ? "passager" : "conducteur");
    }

    @Override
    @Transactional
    public void completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation", "id", bookingId));

        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            throw new BadRequestException("La réservation doit être confirmée pour être complétée");
        }

        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);
        logger.info("Réservation complétée - ID: {}", bookingId);
    }
}
