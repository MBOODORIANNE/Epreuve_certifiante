package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.response.CarbonReportResponse;
import java.util.List;

/**
 * Interface du service de génération des rapports d'impact environnemental.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public interface CarbonReportService {

    CarbonReportResponse generateReport(Long companyId, int month, int year);

    CarbonReportResponse getReport(Long companyId, int month, int year);

    List<CarbonReportResponse> getReportsByCompany(Long companyId);

    List<CarbonReportResponse> getYearlyReports(Long companyId, int year);
}
