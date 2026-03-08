package com.ecomove.ecoMove.dto.response;

import java.time.LocalDateTime;

/**
 * DTO de réponse contenant les informations d'une réservation.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public class BookingResponse {

    private Long id;
    private Long tripId;
    private String tripInfo;
    private Long passengerId;
    private String passengerName;
    private String status;
    private int seatsBooked;
    private String pickupAddress;
    private LocalDateTime createdAt;

    public BookingResponse() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }

    public String getTripInfo() { return tripInfo; }
    public void setTripInfo(String tripInfo) { this.tripInfo = tripInfo; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getSeatsBooked() { return seatsBooked; }
    public void setSeatsBooked(int seatsBooked) { this.seatsBooked = seatsBooked; }

    public String getPickupAddress() { return pickupAddress; }
    public void setPickupAddress(String pickupAddress) { this.pickupAddress = pickupAddress; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
