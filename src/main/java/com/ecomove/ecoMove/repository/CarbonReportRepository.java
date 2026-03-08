package com.ecomove.ecoMove.repository;

import com.ecomove.ecoMove.entity.CarbonReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Repository JPA pour l'entité {@link CarbonReport}.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Repository
public interface CarbonReportRepository extends JpaRepository<CarbonReport, Long> {

    List<CarbonReport> findByCompanyId(Long companyId);

    Optional<CarbonReport> findByCompanyIdAndReportMonthAndReportYear(Long companyId, int month, int year);

    List<CarbonReport> findByCompanyIdAndReportYear(Long companyId, int year);

    List<CarbonReport> findByReportMonthAndReportYear(int month, int year);
}
