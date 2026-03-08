package com.ecomove.ecoMove.dto.response;

import java.time.LocalDateTime;

/**
 * DTO de réponse contenant les données d'un rapport d'impact environnemental.
 *
 * @author MBO
 * @version 1.0.0
 * @since 2026-03-06
 */
public class CarbonReportResponse {

    private Long id;
    private String companyName;
    private Long companyId;
    private int reportMonth;
    private int reportYear;
    private int totalTrips;
    private Double totalDistanceKm;
    private Double totalCO2SavedKg;
    private int totalParticipants;
    private Double averagePassengersPerTrip;
    private Double estimatedMoneySaved;
    private LocalDateTime generatedAt;

    public CarbonReportResponse() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }

    public int getReportMonth() { return reportMonth; }
    public void setReportMonth(int reportMonth) { this.reportMonth = reportMonth; }

    public int getReportYear() { return reportYear; }
    public void setReportYear(int reportYear) { this.reportYear = reportYear; }

    public int getTotalTrips() { return totalTrips; }
    public void setTotalTrips(int totalTrips) { this.totalTrips = totalTrips; }

    public Double getTotalDistanceKm() { return totalDistanceKm; }
    public void setTotalDistanceKm(Double totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; }

    public Double getTotalCO2SavedKg() { return totalCO2SavedKg; }
    public void setTotalCO2SavedKg(Double totalCO2SavedKg) { this.totalCO2SavedKg = totalCO2SavedKg; }

    public int getTotalParticipants() { return totalParticipants; }
    public void setTotalParticipants(int totalParticipants) { this.totalParticipants = totalParticipants; }

    public Double getAveragePassengersPerTrip() { return averagePassengersPerTrip; }
    public void setAveragePassengersPerTrip(Double averagePassengersPerTrip) { this.averagePassengersPerTrip = averagePassengersPerTrip; }

    public Double getEstimatedMoneySaved() { return estimatedMoneySaved; }
    public void setEstimatedMoneySaved(Double estimatedMoneySaved) { this.estimatedMoneySaved = estimatedMoneySaved; }

    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
}
