package com.ecomove.ecoMove.entity;

import com.ecomove.ecoMove.entity.enums.TripStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entité représentant un trajet de covoiturage proposé par un conducteur.
 * <p>
 * Un trajet contient les informations de départ et d'arrivée, les horaires,
 * le nombre de places disponibles et les données nécessaires au calcul
 * de l'impact environnemental (distance, CO2 économisé).
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Conducteur proposant le trajet. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id", nullable = false)
    private User driver;

    /** Adresse de départ du trajet. */
    @Column(nullable = false)
    private String departureAddress;

    /** Latitude GPS du point de départ. */
    @Column(nullable = false)
    private Double departureLat;

    /** Longitude GPS du point de départ. */
    @Column(nullable = false)
    private Double departureLng;

    /** Adresse d'arrivée du trajet (généralement le lieu de travail). */
    @Column(nullable = false)
    private String arrivalAddress;

    /** Latitude GPS du point d'arrivée. */
    @Column(nullable = false)
    private Double arrivalLat;

    /** Longitude GPS du point d'arrivée. */
    @Column(nullable = false)
    private Double arrivalLng;

    /** Date et heure de départ prévues. */
    @Column(nullable = false)
    private LocalDateTime departureTime;

    /** Date et heure d'arrivée estimées. */
    private LocalDateTime estimatedArrivalTime;

    /** Nombre de places disponibles pour les passagers. */
    @Column(nullable = false)
    private int availableSeats;

    /** Prix par place en euros. */
    @Column(precision = 10, scale = 2)
    private BigDecimal pricePerSeat;

    /** Véhicule utilisé pour ce trajet. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    /** Statut actuel du trajet. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TripStatus status = TripStatus.PLANNED;

    /** Distance du trajet en kilomètres. */
    private Double distanceKm;

    /** CO2 économisé estimé en kilogrammes par passager. */
    private Double estimatedCO2SavedKg;

    /** Indique si le trajet est récurrent (quotidien, hebdomadaire). */
    @Column(nullable = false)
    private boolean recurring = false;

    /** Description ou commentaire sur le trajet. */
    @Column(length = 500)
    private String description;

    /** Liste des réservations pour ce trajet. */
    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    /** Date de création de l'enregistrement. */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Date de la dernière mise à jour. */
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /** Constructeur par défaut requis par JPA. */
    public Trip() {
    }

    // ==================== Getters & Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public String getDepartureAddress() {
        return departureAddress;
    }

    public void setDepartureAddress(String departureAddress) {
        this.departureAddress = departureAddress;
    }

    public Double getDepartureLat() {
        return departureLat;
    }

    public void setDepartureLat(Double departureLat) {
        this.departureLat = departureLat;
    }

    public Double getDepartureLng() {
        return departureLng;
    }

    public void setDepartureLng(Double departureLng) {
        this.departureLng = departureLng;
    }

    public String getArrivalAddress() {
        return arrivalAddress;
    }

    public void setArrivalAddress(String arrivalAddress) {
        this.arrivalAddress = arrivalAddress;
    }

    public Double getArrivalLat() {
        return arrivalLat;
    }

    public void setArrivalLat(Double arrivalLat) {
        this.arrivalLat = arrivalLat;
    }

    public Double getArrivalLng() {
        return arrivalLng;
    }

    public void setArrivalLng(Double arrivalLng) {
        this.arrivalLng = arrivalLng;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalDateTime getEstimatedArrivalTime() {
        return estimatedArrivalTime;
    }

    public void setEstimatedArrivalTime(LocalDateTime estimatedArrivalTime) {
        this.estimatedArrivalTime = estimatedArrivalTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public BigDecimal getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(BigDecimal pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public TripStatus getStatus() {
        return status;
    }

    public void setStatus(TripStatus status) {
        this.status = status;
    }

    public Double getDistanceKm() {
        return distanceKm;
    }

    public void setDistanceKm(Double distanceKm) {
        this.distanceKm = distanceKm;
    }

    public Double getEstimatedCO2SavedKg() {
        return estimatedCO2SavedKg;
    }

    public void setEstimatedCO2SavedKg(Double estimatedCO2SavedKg) {
        this.estimatedCO2SavedKg = estimatedCO2SavedKg;
    }

    public boolean isRecurring() {
        return recurring;
    }

    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
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

    /**
     * Calcule le nombre de places restantes en soustrayant les réservations confirmées.
     *
     * @return le nombre de places encore disponibles
     */
    public int getRemainingSeats() {
        long bookedSeats = bookings.stream()
                .filter(b -> b.getStatus() == com.ecomove.ecoMove.entity.enums.BookingStatus.CONFIRMED)
                .mapToInt(Booking::getSeatsBooked)
                .sum();
        return availableSeats - (int) bookedSeats;
    }

    // ==================== equals, hashCode, toString ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trip trip = (Trip) o;
        return Objects.equals(id, trip.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", departureAddress='" + departureAddress + '\'' +
                ", arrivalAddress='" + arrivalAddress + '\'' +
                ", departureTime=" + departureTime +
                ", availableSeats=" + availableSeats +
                ", status=" + status +
                '}';
    }
}
