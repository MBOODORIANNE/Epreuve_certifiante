package com.ecomove.ecoMove.controller;

import com.ecomove.ecoMove.dto.response.CarbonReportResponse;
import com.ecomove.ecoMove.service.CarbonReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour les rapports d'impact environnemental.
 * <p>
 * Accessible uniquement aux administrateurs d'entreprises et à l'administration EcoMove.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@RestController
@RequestMapping("/api/reports")
@Tag(name = "Rapports Environnementaux", description = "Indicateurs de performance environnementale")
@SecurityRequirement(name = "bearerAuth")
public class CarbonReportController {

    private final CarbonReportService carbonReportService;

    public CarbonReportController(CarbonReportService carbonReportService) {
        this.carbonReportService = carbonReportService;
    }

    @PostMapping("/generate")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ECOMOVE_ADMIN')")
    @Operation(summary = "Générer un rapport mensuel d'impact environnemental")
    public ResponseEntity<CarbonReportResponse> generateReport(
            @RequestParam Long companyId,
            @RequestParam int month,
            @RequestParam int year) {
        return new ResponseEntity<>(carbonReportService.generateReport(companyId, month, year), HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ECOMOVE_ADMIN')")
    @Operation(summary = "Récupérer un rapport mensuel")
    public ResponseEntity<CarbonReportResponse> getReport(
            @RequestParam Long companyId,
            @RequestParam int month,
            @RequestParam int year) {
        return ResponseEntity.ok(carbonReportService.getReport(companyId, month, year));
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ECOMOVE_ADMIN')")
    @Operation(summary = "Tous les rapports d'une entreprise")
    public ResponseEntity<List<CarbonReportResponse>> getReportsByCompany(@PathVariable Long companyId) {
        return ResponseEntity.ok(carbonReportService.getReportsByCompany(companyId));
    }

    @GetMapping("/company/{companyId}/year/{year}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ECOMOVE_ADMIN')")
    @Operation(summary = "Rapports annuels d'une entreprise")
    public ResponseEntity<List<CarbonReportResponse>> getYearlyReports(@PathVariable Long companyId,
                                                                        @PathVariable int year) {
        return ResponseEntity.ok(carbonReportService.getYearlyReports(companyId, year));
    }
}
