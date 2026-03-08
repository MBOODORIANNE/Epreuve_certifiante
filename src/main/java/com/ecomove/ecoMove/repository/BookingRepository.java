package com.ecomove.ecoMove.repository;

import com.ecomove.ecoMove.entity.Booking;
import com.ecomove.ecoMove.entity.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repository JPA pour l'entité {@link Booking}.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByPassengerId(Long passengerId);

    List<Booking> findByTripId(Long tripId);

    List<Booking> findByTripIdAndStatus(Long tripId, BookingStatus status);

    List<Booking> findByPassengerIdAndStatus(Long passengerId, BookingStatus status);

    boolean existsByTripIdAndPassengerId(Long tripId, Long passengerId);
}
