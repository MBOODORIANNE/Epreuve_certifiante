package com.ecomove.ecoMove.entity.enums;

/**
 * Énumération représentant les différents statuts possibles d'une réservation de covoiturage.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public enum BookingStatus {

    /** La réservation est en attente de confirmation par le conducteur. */
    PENDING,

    /** La réservation a été confirmée par le conducteur. */
    CONFIRMED,

    /** La réservation a été annulée. */
    CANCELLED,

    /** Le trajet réservé a été effectué avec succès. */
    COMPLETED
}
