package com.ecomove.ecoMove.dto.request;

import com.ecomove.ecoMove.entity.enums.VehicleType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO pour l'enregistrement ou la modification d'un véhicule.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public class VehicleRequest {

    @NotBlank(message = "La marque est obligatoire")
    private String brand;

    @NotBlank(message = "Le modèle est obligatoire")
    private String model;

    private String color;

    @NotBlank(message = "L'immatriculation est obligatoire")
    private String licensePlate;

    @Min(value = 1, message = "Le nombre de places doit être au minimum 1")
    private int seats;

    @NotNull(message = "Le type de véhicule est obligatoire")
    private VehicleType vehicleType;

    private Double fuelConsumption;

    public VehicleRequest() {
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
}
