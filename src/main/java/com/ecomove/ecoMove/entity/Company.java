package com.ecomove.ecoMove.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entité représentant une entreprise partenaire de la plateforme EcoMove.
 * <p>
 * Chaque entreprise partenaire peut inscrire ses employés sur la plateforme
 * et recevoir des rapports détaillés sur l'impact environnemental
 * des covoiturages effectués par ses collaborateurs.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Entity
@Table(name = "companies")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nom de l'entreprise partenaire. */
    @Column(nullable = false)
    private String name;

    /** Numéro SIRET unique de l'entreprise. */
    @Column(nullable = false, unique = true, length = 14)
    private String siret;

    /** Adresse postale du siège de l'entreprise. */
    @Column(nullable = false)
    private String address;

    /** Latitude GPS du siège de l'entreprise. */
    private Double latitude;

    /** Longitude GPS du siège de l'entreprise. */
    private Double longitude;

    /** Adresse email de contact de l'entreprise. */
    @Column(nullable = false, unique = true)
    private String email;

    /** Numéro de téléphone de l'entreprise. */
    private String phone;

    /** Indique si le compte entreprise est actif. */
    @Column(nullable = false)
    private boolean active = true;

    /** Liste des employés rattachés à cette entreprise. */
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> employees = new ArrayList<>();

    /** Liste des rapports carbone générés pour cette entreprise. */
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CarbonReport> carbonReports = new ArrayList<>();

    /** Date de création de l'enregistrement. */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Date de la dernière mise à jour. */
    private LocalDateTime updatedAt;

    /**
     * Initialise automatiquement les dates de création et de mise à jour.
     */
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Met à jour automatiquement la date de mise à jour.
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /** Constructeur par défaut requis par JPA. */
    public Company() {
    }

    /**
     * Constructeur avec les champs obligatoires.
     *
     * @param name    le nom de l'entreprise
     * @param siret   le numéro SIRET
     * @param address l'adresse postale
     * @param email   l'adresse email de contact
     */
    public Company(String name, String siret, String address, String email) {
        this.name = name;
        this.siret = siret;
        this.address = address;
        this.email = email;
    }

    // ==================== Getters & Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<User> getEmployees() {
        return employees;
    }

    public void setEmployees(List<User> employees) {
        this.employees = employees;
    }

    public List<CarbonReport> getCarbonReports() {
        return carbonReports;
    }

    public void setCarbonReports(List<CarbonReport> carbonReports) {
        this.carbonReports = carbonReports;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // ==================== equals, hashCode, toString ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return Objects.equals(id, company.id) && Objects.equals(siret, company.siret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, siret);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", siret='" + siret + '\'' +
                ", email='" + email + '\'' +
                ", active=" + active +
                '}';
    }
}
