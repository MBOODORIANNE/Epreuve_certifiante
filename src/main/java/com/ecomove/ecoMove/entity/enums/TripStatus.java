package com.ecomove.ecoMove.entity.enums;

/**
 * Énumération représentant les différents statuts possibles d'un trajet.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public enum TripStatus {

    /** Le trajet est planifié mais n'a pas encore commencé. */
    PLANNED,

    /** Le trajet est en cours de réalisation. */
    IN_PROGRESS,

    /** Le trajet a été complété avec succès. */
    COMPLETED,

    /** Le trajet a été annulé. */
    CANCELLED
}
