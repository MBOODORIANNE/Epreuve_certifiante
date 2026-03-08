package com.ecomove.ecoMove.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * DTO pour la création d'une réservation de covoiturage.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public class BookingRequest {

    @NotNull(message = "L'identifiant du trajet est obligatoire")
    private Long tripId;

    @Min(value = 1, message = "Le nombre de places doit être au minimum 1")
    private int seatsBooked = 1;

    private String pickupAddress;
    private Double pickupLatitude;
    private Double pickupLongitude;

    public BookingRequest() {
    }

    public BookingRequest(Long tripId, int seatsBooked) {
        this.tripId = tripId;
        this.seatsBooked = seatsBooked;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
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
}
