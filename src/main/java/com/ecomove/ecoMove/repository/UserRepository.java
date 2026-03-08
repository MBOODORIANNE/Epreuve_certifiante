package com.ecomove.ecoMove.repository;

import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.entity.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link User}.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByCompanyId(Long companyId);

    List<User> findByRole(Role role);

    List<User> findByCompanyIdAndActiveTrue(Long companyId);

    List<User> findByActiveTrue();
}
