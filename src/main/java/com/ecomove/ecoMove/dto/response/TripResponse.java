package com.ecomove.ecoMove.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de réponse contenant les informations d'un trajet de covoiturage.
 *
 * @author MBO
 * @version 1.0.0
 * @since 2026-03-06
 */
public class TripResponse {

    private Long id;
    private String driverName;
    private Long driverId;
    private String departureAddress;
    private Double departureLat;
    private Double departureLng;
    private String arrivalAddress;
    private Double arrivalLat;
    private Double arrivalLng;
    private LocalDateTime departureTime;
    private LocalDateTime estimatedArrivalTime;
    private int availableSeats;
    private int remainingSeats;
    private BigDecimal pricePerSeat;
    private String vehicleInfo;
    private String status;
    private Double distanceKm;
    private Double estimatedCO2SavedKg;
    private boolean recurring;
    private String description;
    private int totalBookings;
    private LocalDateTime createdAt;

    public TripResponse() {
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Long getDriverId() {
        return driverId;
    }

    public void setDriverId(Long driverId) {
        this.driverId = driverId;
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

    public int getRemainingSeats() {
        return remainingSeats;
    }

    public void setRemainingSeats(int remainingSeats) {
        this.remainingSeats = remainingSeats;
    }

    public BigDecimal getPricePerSeat() {
        return pricePerSeat;
    }

    public void setPricePerSeat(BigDecimal pricePerSeat) {
        this.pricePerSeat = pricePerSeat;
    }

    public String getVehicleInfo() {
        return vehicleInfo;
    }

    public void setVehicleInfo(String vehicleInfo) {
        this.vehicleInfo = vehicleInfo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public int getTotalBookings() {
        return totalBookings;
    }

    public void setTotalBookings(int totalBookings) {
        this.totalBookings = totalBookings;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
