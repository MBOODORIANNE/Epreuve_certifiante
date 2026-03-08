package com.ecomove.ecoMove.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant un rapport d'impact environnemental pour une entreprise partenaire.
 * <p>
 * Les rapports carbone sont générés mensuellement et fournissent aux entreprises
 * des indicateurs concrets sur l'utilisation du covoiturage par leurs employés
 * et la réduction de CO2 associée.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Entity
@Table(name = "carbon_reports", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"company_id", "report_month", "report_year"})
})
public class CarbonReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Entreprise concernée par le rapport. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    /** Mois du rapport (1-12). */
    @Column(name = "report_month", nullable = false)
    private int reportMonth;

    /** Année du rapport. */
    @Column(name = "report_year", nullable = false)
    private int reportYear;

    /** Nombre total de trajets effectués en covoiturage. */
    @Column(nullable = false)
    private int totalTrips;

    /** Distance totale parcourue en covoiturage (en km). */
    @Column(nullable = false)
    private Double totalDistanceKm;

    /** CO2 total économisé grâce au covoiturage (en kg). */
    @Column(nullable = false)
    private Double totalCO2SavedKg;

    /** Nombre total d'employés ayant participé au covoiturage. */
    @Column(nullable = false)
    private int totalParticipants;

    /** Nombre moyen de passagers par trajet. */
    private Double averagePassengersPerTrip;

    /** Économie financière estimée pour les employés (en euros). */
    private Double estimatedMoneySaved;

    /** Date de génération du rapport. */
    @Column(nullable = false, updatable = false)
    private LocalDateTime generatedAt;

    @PrePersist
    protected void onCreate() {
        this.generatedAt = LocalDateTime.now();
    }

    /** Constructeur par défaut requis par JPA. */
    public CarbonReport() {
    }

    /**
     * Constructeur avec les champs obligatoires.
     *
     * @param company           l'entreprise concernée
     * @param reportMonth       le mois du rapport
     * @param reportYear        l'année du rapport
     * @param totalTrips        le nombre total de trajets
     * @param totalDistanceKm   la distance totale en km
     * @param totalCO2SavedKg   le CO2 économisé en kg
     * @param totalParticipants le nombre de participants
     */
    public CarbonReport(Company company, int reportMonth, int reportYear, int totalTrips,
                        Double totalDistanceKm, Double totalCO2SavedKg, int totalParticipants) {
        this.company = company;
        this.reportMonth = reportMonth;
        this.reportYear = reportYear;
        this.totalTrips = totalTrips;
        this.totalDistanceKm = totalDistanceKm;
        this.totalCO2SavedKg = totalCO2SavedKg;
        this.totalParticipants = totalParticipants;
    }

    // ==================== Getters & Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public int getReportMonth() {
        return reportMonth;
    }

    public void setReportMonth(int reportMonth) {
        this.reportMonth = reportMonth;
    }

    public int getReportYear() {
        return reportYear;
    }

    public void setReportYear(int reportYear) {
        this.reportYear = reportYear;
    }

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }

    public Double getTotalDistanceKm() {
        return totalDistanceKm;
    }

    public void setTotalDistanceKm(Double totalDistanceKm) {
        this.totalDistanceKm = totalDistanceKm;
    }

    public Double getTotalCO2SavedKg() {
        return totalCO2SavedKg;
    }

    public void setTotalCO2SavedKg(Double totalCO2SavedKg) {
        this.totalCO2SavedKg = totalCO2SavedKg;
    }

    public int getTotalParticipants() {
        return totalParticipants;
    }

    public void setTotalParticipants(int totalParticipants) {
        this.totalParticipants = totalParticipants;
    }

    public Double getAveragePassengersPerTrip() {
        return averagePassengersPerTrip;
    }

    public void setAveragePassengersPerTrip(Double averagePassengersPerTrip) {
        this.averagePassengersPerTrip = averagePassengersPerTrip;
    }

    public Double getEstimatedMoneySaved() {
        return estimatedMoneySaved;
    }

    public void setEstimatedMoneySaved(Double estimatedMoneySaved) {
        this.estimatedMoneySaved = estimatedMoneySaved;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    // ==================== equals, hashCode, toString ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarbonReport that = (CarbonReport) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "CarbonReport{" +
                "id=" + id +
                ", reportMonth=" + reportMonth +
                ", reportYear=" + reportYear +
                ", totalTrips=" + totalTrips +
                ", totalCO2SavedKg=" + totalCO2SavedKg +
                '}';
    }
}
