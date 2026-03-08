package com.ecomove.ecoMove.entity;

import com.ecomove.ecoMove.entity.enums.VehicleType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entité représentant un véhicule enregistré sur la plateforme EcoMove.
 * <p>
 * Chaque véhicule est rattaché à un propriétaire (conducteur) et possède un type
 * qui influence le calcul de l'empreinte carbone économisée lors des covoiturages.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Entity
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Marque du véhicule (ex: Renault, Tesla, Toyota). */
    @Column(nullable = false)
    private String brand;

    /** Modèle du véhicule (ex: Clio, Model 3, Yaris). */
    @Column(nullable = false)
    private String model;

    /** Couleur du véhicule. */
    private String color;

    /** Immatriculation unique du véhicule. */
    @Column(nullable = false, unique = true)
    private String licensePlate;

    /** Nombre de places passagers disponibles. */
    @Column(nullable = false)
    private int seats;

    /** Type de véhicule influençant le calcul de CO2. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType vehicleType;

    /** Consommation moyenne en litres/100km (ou kWh/100km pour les électriques). */
    private Double fuelConsumption;

    /** Propriétaire du véhicule. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    /** Date d'enregistrement du véhicule. */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    /** Constructeur par défaut requis par JPA. */
    public Vehicle() {
    }

    /**
     * Constructeur avec les champs obligatoires.
     *
     * @param brand        la marque
     * @param model        le modèle
     * @param licensePlate l'immatriculation
     * @param seats        le nombre de places
     * @param vehicleType  le type de véhicule
     * @param owner        le propriétaire
     */
    public Vehicle(String brand, String model, String licensePlate, int seats, VehicleType vehicleType, User owner) {
        this.brand = brand;
        this.model = model;
        this.licensePlate = licensePlate;
        this.seats = seats;
        this.vehicleType = vehicleType;
        this.owner = owner;
    }

    // ==================== Getters & Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Double getFuelConsumption() {
        return fuelConsumption;
    }

    public void setFuelConsumption(Double fuelConsumption) {
        this.fuelConsumption = fuelConsumption;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ==================== equals, hashCode, toString ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return Objects.equals(id, vehicle.id) && Objects.equals(licensePlate, vehicle.licensePlate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, licensePlate);
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", brand='" + brand + '\'' +
                ", model='" + model + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", vehicleType=" + vehicleType +
                '}';
    }
}
