package com.ecomove.ecoMove.controller;

import com.ecomove.ecoMove.dto.request.CompanyRequest;
import com.ecomove.ecoMove.dto.response.CompanyResponse;
import com.ecomove.ecoMove.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des entreprises partenaires.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@RestController
@RequestMapping("/api/companies")
@Tag(name = "Entreprises", description = "Gestion des entreprises partenaires")
@SecurityRequirement(name = "bearerAuth")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ECOMOVE_ADMIN')")
    @Operation(summary = "Créer une entreprise partenaire (Admin EcoMove)")
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest request) {
        return new ResponseEntity<>(companyService.createCompany(request), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une entreprise par ID")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @GetMapping
    @Operation(summary = "Lister toutes les entreprises partenaires")
    public ResponseEntity<List<CompanyResponse>> getAllCompanies() {
        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des entreprises par nom")
    public ResponseEntity<List<CompanyResponse>> searchCompanies(@RequestParam String name) {
        return ResponseEntity.ok(companyService.searchCompanies(name));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('COMPANY_ADMIN', 'ECOMOVE_ADMIN')")
    @Operation(summary = "Modifier une entreprise")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id,
                                                          @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.updateCompany(id, request));
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ECOMOVE_ADMIN')")
    @Operation(summary = "Désactiver une entreprise (Admin EcoMove)")
    public ResponseEntity<Void> deactivateCompany(@PathVariable Long id) {
        companyService.deactivateCompany(id);
        return ResponseEntity.noContent().build();
    }
}
