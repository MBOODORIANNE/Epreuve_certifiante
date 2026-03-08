package com.ecomove.ecoMove.repository;

import com.ecomove.ecoMove.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link Vehicle}.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByOwnerId(Long ownerId);

    Optional<Vehicle> findByLicensePlate(String licensePlate);

    boolean existsByLicensePlate(String licensePlate);
}
