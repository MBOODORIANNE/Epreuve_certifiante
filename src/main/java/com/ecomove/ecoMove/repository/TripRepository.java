package com.ecomove.ecoMove.repository;

import com.ecomove.ecoMove.entity.Trip;
import com.ecomove.ecoMove.entity.enums.TripStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository JPA pour l'entité {@link Trip}.
 * <p>
 * Inclut des requêtes personnalisées pour la recherche géographique
 * et le matching de covoitureurs.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    List<Trip> findByDriverId(Long driverId);

    List<Trip> findByStatus(TripStatus status);

    List<Trip> findByDriverIdAndStatus(Long driverId, TripStatus status);

    /**
     * Recherche les trajets disponibles dont le départ est dans un rayon donné
     * et avec des dates correspondantes.
     *
     * @param departureLat   latitude du point de départ recherché
     * @param departureLng   longitude du point de départ recherché
     * @param arrivalLat     latitude du point d'arrivée recherché
     * @param arrivalLng     longitude du point d'arrivée recherché
     * @param radiusKm       rayon de recherche en km
     * @param departureStart début de la fenêtre horaire de départ
     * @param departureEnd   fin de la fenêtre horaire de départ
     * @return la liste des trajets correspondants
     */
    @Query("SELECT t FROM Trip t WHERE t.status = 'PLANNED' " +
            "AND t.departureTime BETWEEN :departureStart AND :departureEnd " +
            "AND (6371 * acos(cos(radians(:departureLat)) * cos(radians(t.departureLat)) * " +
            "cos(radians(t.departureLng) - radians(:departureLng)) + " +
            "sin(radians(:departureLat)) * sin(radians(t.departureLat)))) < :radiusKm " +
            "AND (6371 * acos(cos(radians(:arrivalLat)) * cos(radians(t.arrivalLat)) * " +
            "cos(radians(t.arrivalLng) - radians(:arrivalLng)) + " +
            "sin(radians(:arrivalLat)) * sin(radians(t.arrivalLat)))) < :radiusKm " +
            "ORDER BY t.departureTime ASC")
    List<Trip> searchTrips(
            @Param("departureLat") Double departureLat,
            @Param("departureLng") Double departureLng,
            @Param("arrivalLat") Double arrivalLat,
            @Param("arrivalLng") Double arrivalLng,
            @Param("radiusKm") Double radiusKm,
            @Param("departureStart") LocalDateTime departureStart,
            @Param("departureEnd") LocalDateTime departureEnd
    );

    /**
     * Compte le nombre de trajets effectués par les employés d'une entreprise sur un mois donné.
     */
    @Query("SELECT COUNT(t) FROM Trip t WHERE t.driver.company.id = :companyId " +
            "AND t.status = 'COMPLETED' " +
            "AND MONTH(t.departureTime) = :month AND YEAR(t.departureTime) = :year")
    int countCompletedTripsForCompany(
            @Param("companyId") Long companyId,
            @Param("month") int month,
            @Param("year") int year
    );

    /**
     * Récupère les trajets complétés pour une entreprise sur un mois donné.
     */
    @Query("SELECT t FROM Trip t WHERE t.driver.company.id = :companyId " +
            "AND t.status = 'COMPLETED' " +
            "AND MONTH(t.departureTime) = :month AND YEAR(t.departureTime) = :year")
    List<Trip> findCompletedTripsForCompany(
            @Param("companyId") Long companyId,
            @Param("month") int month,
            @Param("year") int year
    );

    List<Trip> findByDepartureTimeBetweenAndStatus(LocalDateTime start, LocalDateTime end, TripStatus status);
}
