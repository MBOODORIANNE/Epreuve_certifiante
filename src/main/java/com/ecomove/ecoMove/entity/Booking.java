package com.ecomove.ecoMove.entity;

import com.ecomove.ecoMove.entity.enums.BookingStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant une réservation de covoiturage par un passager.
 * <p>
 * Une réservation lie un passager à un trajet proposé par un conducteur.
 * Elle suit un cycle de vie : PENDING → CONFIRMED → COMPLETED ou CANCELLED.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Trajet réservé. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id", nullable = false)
    private Trip trip;

    /** Passager ayant effectué la réservation. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "passenger_id", nullable = false)
    private User passenger;

    /** Statut actuel de la réservation. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status = BookingStatus.PENDING;

    /** Nombre de places réservées. */
    @Column(nullable = false)
    private int seatsBooked = 1;

    /** Point de ramassage souhaité par le passager. */
    private String pickupAddress;

    /** Latitude GPS du point de ramassage. */
    private Double pickupLatitude;

    /** Longitude GPS du point de ramassage. */
    private Double pickupLongitude;

    /** Date de création de la réservation. */
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
    public Booking() {
    }

    /**
     * Constructeur avec les champs obligatoires.
     *
     * @param trip      le trajet réservé
     * @param passenger le passager
     * @param seatsBooked le nombre de places réservées
     */
    public Booking(Trip trip, User passenger, int seatsBooked) {
        this.trip = trip;
        this.passenger = passenger;
        this.seatsBooked = seatsBooked;
    }

    // ==================== Getters & Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(int seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public Double getPickupLatitude() {
        return pickupLatitude;
    }

    public void setPickupLatitude(Double pickupLatitude) {
        this.pickupLatitude = pickupLatitude;
    }

    public Double getPickupLongitude() {
        return pickupLongitude;
    }

    public void setPickupLongitude(Double pickupLongitude) {
        this.pickupLongitude = pickupLongitude;
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
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", status=" + status +
                ", seatsBooked=" + seatsBooked +
                '}';
    }
}
