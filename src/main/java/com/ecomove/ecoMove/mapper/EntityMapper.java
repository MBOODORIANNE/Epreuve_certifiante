package com.ecomove.ecoMove.mapper;

import com.ecomove.ecoMove.dto.response.*;
import com.ecomove.ecoMove.entity.*;
import com.ecomove.ecoMove.entity.enums.BookingStatus;

/**
 * Classe utilitaire de mapping entre les entités JPA et les DTOs de réponse.
 * <p>
 * Centralise toute la logique de conversion pour assurer la cohérence
 * des données retournées par l'API REST.
 * </p>
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public final class EntityMapper {

    private EntityMapper() {
        // Classe utilitaire - pas d'instanciation
    }

    /**
     * Convertit une entité User en UserResponse.
     *
     * @param user l'entité User à convertir
     * @return le DTO UserResponse correspondant
     */
    public static UserResponse toUserResponse(User user) {
        if (user == null) return null;

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setPhone(user.getPhone());
        response.setHomeAddress(user.getHomeAddress());
        response.setHomeLatitude(user.getHomeLatitude());
        response.setHomeLongitude(user.getHomeLongitude());
        response.setRole(user.getRole().name());
        response.setActive(user.isActive());
        response.setProfilePictureUrl(user.getProfilePictureUrl());
        response.setSmokingAllowed(user.isSmokingAllowed());
        response.setMusicAllowed(user.isMusicAllowed());
        response.setPetsAllowed(user.isPetsAllowed());
        response.setCreatedAt(user.getCreatedAt());

        if (user.getCompany() != null) {
            response.setCompanyName(user.getCompany().getName());
            response.setCompanyId(user.getCompany().getId());
        }

        return response;
    }

    /**
     * Convertit une entité Company en CompanyResponse.
     *
     * @param company l'entité Company à convertir
     * @return le DTO CompanyResponse correspondant
     */
    public static CompanyResponse toCompanyResponse(Company company) {
        if (company == null) return null;

        CompanyResponse response = new CompanyResponse();
        response.setId(company.getId());
        response.setName(company.getName());
        response.setSiret(company.getSiret());
        response.setAddress(company.getAddress());
        response.setLatitude(company.getLatitude());
        response.setLongitude(company.getLongitude());
        response.setEmail(company.getEmail());
        response.setPhone(company.getPhone());
        response.setActive(company.isActive());
        response.setCreatedAt(company.getCreatedAt());

        if (company.getEmployees() != null) {
            response.setEmployeeCount(company.getEmployees().size());
        }

        return response;
    }

    /**
     * Convertit une entité Trip en TripResponse.
     *
     * @param trip l'entité Trip à convertir
     * @return le DTO TripResponse correspondant
     */
    public static TripResponse toTripResponse(Trip trip) {
        if (trip == null) return null;

        TripResponse response = new TripResponse();
        response.setId(trip.getId());
        response.setDepartureAddress(trip.getDepartureAddress());
        response.setDepartureLat(trip.getDepartureLat());
        response.setDepartureLng(trip.getDepartureLng());
        response.setArrivalAddress(trip.getArrivalAddress());
        response.setArrivalLat(trip.getArrivalLat());
        response.setArrivalLng(trip.getArrivalLng());
        response.setDepartureTime(trip.getDepartureTime());
        response.setEstimatedArrivalTime(trip.getEstimatedArrivalTime());
        response.setAvailableSeats(trip.getAvailableSeats());
        response.setRemainingSeats(trip.getRemainingSeats());
        response.setPricePerSeat(trip.getPricePerSeat());
        response.setStatus(trip.getStatus().name());
        response.setDistanceKm(trip.getDistanceKm());
        response.setEstimatedCO2SavedKg(trip.getEstimatedCO2SavedKg());
        response.setRecurring(trip.isRecurring());
        response.setDescription(trip.getDescription());
        response.setCreatedAt(trip.getCreatedAt());

        if (trip.getDriver() != null) {
            response.setDriverName(trip.getDriver().getFullName());
            response.setDriverId(trip.getDriver().getId());
        }

        if (trip.getVehicle() != null) {
            Vehicle v = trip.getVehicle();
            response.setVehicleInfo(v.getBrand() + " " + v.getModel() + " (" + v.getColor() + ")");
        }

        if (trip.getBookings() != null) {
            response.setTotalBookings((int) trip.getBookings().stream()
                    .filter(b -> b.getStatus() == BookingStatus.CONFIRMED)
                    .count());
        }

        return response;
    }

    /**
     * Convertit une entité Booking en BookingResponse.
     *
     * @param booking l'entité Booking à convertir
     * @return le DTO BookingResponse correspondant
     */
    public static BookingResponse toBookingResponse(Booking booking) {
        if (booking == null) return null;

        BookingResponse response = new BookingResponse();
        response.setId(booking.getId());
        response.setStatus(booking.getStatus().name());
        response.setSeatsBooked(booking.getSeatsBooked());
        response.setPickupAddress(booking.getPickupAddress());
        response.setCreatedAt(booking.getCreatedAt());

        if (booking.getTrip() != null) {
            response.setTripId(booking.getTrip().getId());
            response.setTripInfo(booking.getTrip().getDepartureAddress() + " → " + booking.getTrip().getArrivalAddress());
        }

        if (booking.getPassenger() != null) {
            response.setPassengerId(booking.getPassenger().getId());
            response.setPassengerName(booking.getPassenger().getFullName());
        }

        return response;
    }

    /**
     * Convertit une entité Message en MessageResponse.
     *
     * @param message l'entité Message à convertir
     * @return le DTO MessageResponse correspondant
     */
    public static MessageResponse toMessageResponse(Message message) {
        if (message == null) return null;

        MessageResponse response = new MessageResponse();
        response.setId(message.getId());
        response.setContent(message.getContent());
        response.setRead(message.isRead());
        response.setCreatedAt(message.getCreatedAt());

        if (message.getSender() != null) {
            response.setSenderId(message.getSender().getId());
            response.setSenderName(message.getSender().getFullName());
        }

        if (message.getReceiver() != null) {
            response.setReceiverId(message.getReceiver().getId());
            response.setReceiverName(message.getReceiver().getFullName());
        }

        if (message.getTrip() != null) {
            response.setTripId(message.getTrip().getId());
        }

        return response;
    }

    /**
     * Convertit une entité CarbonReport en CarbonReportResponse.
     *
     * @param report l'entité CarbonReport à convertir
     * @return le DTO CarbonReportResponse correspondant
     */
    public static CarbonReportResponse toCarbonReportResponse(CarbonReport report) {
        if (report == null) return null;

        CarbonReportResponse response = new CarbonReportResponse();
        response.setId(report.getId());
        response.setReportMonth(report.getReportMonth());
        response.setReportYear(report.getReportYear());
        response.setTotalTrips(report.getTotalTrips());
        response.setTotalDistanceKm(report.getTotalDistanceKm());
        response.setTotalCO2SavedKg(report.getTotalCO2SavedKg());
        response.setTotalParticipants(report.getTotalParticipants());
        response.setAveragePassengersPerTrip(report.getAveragePassengersPerTrip());
        response.setEstimatedMoneySaved(report.getEstimatedMoneySaved());
        response.setGeneratedAt(report.getGeneratedAt());

        if (report.getCompany() != null) {
            response.setCompanyName(report.getCompany().getName());
            response.setCompanyId(report.getCompany().getId());
        }

        return response;
    }
}
