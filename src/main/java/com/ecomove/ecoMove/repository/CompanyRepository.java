package com.ecomove.ecoMove.repository;

import com.ecomove.ecoMove.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repository JPA pour l'entité {@link Company}.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findBySiret(String siret);

    Optional<Company> findByEmail(String email);

    boolean existsBySiret(String siret);

    boolean existsByEmail(String email);

    List<Company> findByActiveTrue();

    List<Company> findByNameContainingIgnoreCase(String name);
}
