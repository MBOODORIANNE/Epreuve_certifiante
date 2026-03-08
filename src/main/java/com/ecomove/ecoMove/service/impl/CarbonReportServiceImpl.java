package com.ecomove.ecoMove.service.impl;

import com.ecomove.ecoMove.dto.response.CarbonReportResponse;
import com.ecomove.ecoMove.entity.Booking;
import com.ecomove.ecoMove.entity.CarbonReport;
import com.ecomove.ecoMove.entity.Company;
import com.ecomove.ecoMove.entity.Trip;
import com.ecomove.ecoMove.entity.enums.BookingStatus;
import com.ecomove.ecoMove.exception.ResourceNotFoundException;
import com.ecomove.ecoMove.mapper.EntityMapper;
import com.ecomove.ecoMove.repository.CarbonReportRepository;
import com.ecomove.ecoMove.repository.CompanyRepository;
import com.ecomove.ecoMove.repository.TripRepository;
import com.ecomove.ecoMove.service.CarbonReportService;
import com.ecomove.ecoMove.util.CarbonCalculator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implémentation du service de génération des rapports d'impact environnemental.
 * <p>
 * Calcule les indicateurs clés à partir des trajets complétés :
 * CO2 économisé, distance parcourue, nombre de participants, etc.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Service
public class CarbonReportServiceImpl implements CarbonReportService {

    private static final Logger logger = LoggerFactory.getLogger(CarbonReportServiceImpl.class);

    private final CarbonReportRepository carbonReportRepository;
    private final CompanyRepository companyRepository;
    private final TripRepository tripRepository;

    public CarbonReportServiceImpl(CarbonReportRepository carbonReportRepository,
                                   CompanyRepository companyRepository, TripRepository tripRepository) {
        this.carbonReportRepository = carbonReportRepository;
        this.companyRepository = companyRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    @Transactional
    public CarbonReportResponse generateReport(Long companyId, int month, int year) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Entreprise", "id", companyId));

        List<Trip> completedTrips = tripRepository.findCompletedTripsForCompany(companyId, month, year);

        int totalTrips = completedTrips.size();
        double totalDistanceKm = 0.0;
        double totalCO2SavedKg = 0.0;
        Set<Long> participants = new HashSet<>();
        int totalPassengers = 0;
        double totalMoneySaved = 0.0;

        for (Trip trip : completedTrips) {
            double distance = trip.getDistanceKm() != null ? trip.getDistanceKm() : 0.0;
            totalDistanceKm += distance;

            int confirmedPassengers = (int) trip.getBookings().stream()
                    .filter(b -> b.getStatus() == BookingStatus.COMPLETED || b.getStatus() == BookingStatus.CONFIRMED)
                    .mapToInt(Booking::getSeatsBooked)
                    .sum();

            totalPassengers += confirmedPassengers;

            if (trip.getEstimatedCO2SavedKg() != null) {
                totalCO2SavedKg += trip.getEstimatedCO2SavedKg() * confirmedPassengers;
            }

            totalMoneySaved += CarbonCalculator.estimateMoneySaved(distance, confirmedPassengers);

            // Compter les participants uniques
            participants.add(trip.getDriver().getId());
            trip.getBookings().stream()
                    .filter(b -> b.getStatus() == BookingStatus.COMPLETED || b.getStatus() == BookingStatus.CONFIRMED)
                    .forEach(b -> participants.add(b.getPassenger().getId()));
        }

        double averagePassengers = totalTrips > 0 ? (double) totalPassengers / totalTrips : 0.0;

        // Créer ou mettre à jour le rapport
        CarbonReport report = carbonReportRepository
                .findByCompanyIdAndReportMonthAndReportYear(companyId, month, year)
                .orElse(new CarbonReport());

        report.setCompany(company);
        report.setReportMonth(month);
        report.setReportYear(year);
        report.setTotalTrips(totalTrips);
        report.setTotalDistanceKm(Math.round(totalDistanceKm * 100.0) / 100.0);
        report.setTotalCO2SavedKg(Math.round(totalCO2SavedKg * 100.0) / 100.0);
        report.setTotalParticipants(participants.size());
        report.setAveragePassengersPerTrip(Math.round(averagePassengers * 100.0) / 100.0);
        report.setEstimatedMoneySaved(Math.round(totalMoneySaved * 100.0) / 100.0);

        CarbonReport savedReport = carbonReportRepository.save(report);
        logger.info("Rapport carbone généré - Entreprise: {}, Mois: {}/{}, CO2 économisé: {} kg",
                company.getName(), month, year, totalCO2SavedKg);

        return EntityMapper.toCarbonReportResponse(savedReport);
    }

    @Override
    @Transactional(readOnly = true)
    public CarbonReportResponse getReport(Long companyId, int month, int year) {
        CarbonReport report = carbonReportRepository
                .findByCompanyIdAndReportMonthAndReportYear(companyId, month, year)
                .orElseThrow(() -> new ResourceNotFoundException("Rapport", "période", month + "/" + year));
        return EntityMapper.toCarbonReportResponse(report);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarbonReportResponse> getReportsByCompany(Long companyId) {
        return carbonReportRepository.findByCompanyId(companyId).stream()
                .map(EntityMapper::toCarbonReportResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarbonReportResponse> getYearlyReports(Long companyId, int year) {
        return carbonReportRepository.findByCompanyIdAndReportYear(companyId, year).stream()
                .map(EntityMapper::toCarbonReportResponse)
                .collect(Collectors.toList());
    }
}
