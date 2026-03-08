package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.request.CompanyRequest;
import com.ecomove.ecoMove.dto.response.CompanyResponse;
import java.util.List;

/**
 * Interface du service de gestion des entreprises partenaires.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public interface CompanyService {

    CompanyResponse createCompany(CompanyRequest request);

    CompanyResponse getCompanyById(Long id);

    List<CompanyResponse> getAllCompanies();

    CompanyResponse updateCompany(Long id, CompanyRequest request);

    void deactivateCompany(Long id);

    List<CompanyResponse> searchCompanies(String name);
}
