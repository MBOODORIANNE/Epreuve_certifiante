package com.ecomove.ecoMove.service.impl;

import com.ecomove.ecoMove.dto.request.CompanyRequest;
import com.ecomove.ecoMove.dto.response.CompanyResponse;
import com.ecomove.ecoMove.entity.Company;
import com.ecomove.ecoMove.exception.BadRequestException;
import com.ecomove.ecoMove.exception.ResourceNotFoundException;
import com.ecomove.ecoMove.mapper.EntityMapper;
import com.ecomove.ecoMove.repository.CompanyRepository;
import com.ecomove.ecoMove.service.CompanyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des entreprises partenaires.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    private static final Logger logger = LoggerFactory.getLogger(CompanyServiceImpl.class);
    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public CompanyResponse createCompany(CompanyRequest request) {
        if (companyRepository.existsBySiret(request.getSiret())) {
            throw new BadRequestException("Une entreprise avec ce SIRET existe déjà : " + request.getSiret());
        }
        if (companyRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Une entreprise avec cet email existe déjà : " + request.getEmail());
        }

        Company company = new Company(request.getName(), request.getSiret(), request.getAddress(), request.getEmail());
        company.setLatitude(request.getLatitude());
        company.setLongitude(request.getLongitude());
        company.setPhone(request.getPhone());

        Company savedCompany = companyRepository.save(company);
        logger.info("Entreprise partenaire créée - ID: {}, Nom: {}", savedCompany.getId(), savedCompany.getName());

        return EntityMapper.toCompanyResponse(savedCompany);
    }

    @Override
    @Transactional(readOnly = true)
    public CompanyResponse getCompanyById(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise", "id", id));
        return EntityMapper.toCompanyResponse(company);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll().stream()
                .map(EntityMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CompanyResponse updateCompany(Long id, CompanyRequest request) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise", "id", id));

        company.setName(request.getName());
        company.setAddress(request.getAddress());
        company.setEmail(request.getEmail());
        company.setPhone(request.getPhone());
        company.setLatitude(request.getLatitude());
        company.setLongitude(request.getLongitude());

        Company updatedCompany = companyRepository.save(company);
        logger.info("Entreprise mise à jour - ID: {}", id);

        return EntityMapper.toCompanyResponse(updatedCompany);
    }

    @Override
    @Transactional
    public void deactivateCompany(Long id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise", "id", id));
        company.setActive(false);
        companyRepository.save(company);
        logger.info("Entreprise désactivée - ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CompanyResponse> searchCompanies(String name) {
        return companyRepository.findByNameContainingIgnoreCase(name).stream()
                .map(EntityMapper::toCompanyResponse)
                .collect(Collectors.toList());
    }
}
