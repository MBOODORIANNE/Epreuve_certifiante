package com.ecomove.ecoMove.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO pour la création ou modification d'un trajet de covoiturage.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public class TripRequest {

    @NotBlank(message = "L'adresse de départ est obligatoire")
    private String departureAddress;

    @NotNull(message = "La latitude de départ est obligatoire")
    private Double departureLat;

    @NotNull(message = "La longitude de départ est obligatoire")
    private Double departureLng;

    @NotBlank(message = "L'adresse d'arrivée est obligatoire")
    private String arrivalAddress;

    @NotNull(message = "La latitude d'arrivée est obligatoire")
    private Double arrivalLat;

    @NotNull(message = "La longitude d'arrivée est obligatoire")
    private Double arrivalLng;

    @NotNull(message = "La date de départ est obligatoire")
    @Future(message = "La date de départ doit être dans le futur")
    private LocalDateTime departureTime;

    private LocalDateTime estimatedArrivalTime;

    @Min(value = 1, message = "Le nombre de places doit être au minimum 1")
    private int availableSeats;

    private BigDecimal pricePerSeat;

    private Long vehicleId;

    private boolean recurring = false;

    private String description;

    public TripRequest() {
    }

    // Getters & Setters

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

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
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
}
