package com.ecomove.ecoMove.entity.enums;

/**
 * Énumération représentant les différents types de véhicules supportés par la plateforme.
 * <p>
 * Le type de véhicule est utilisé dans le calcul de l'empreinte carbone économisée
 * et dans le matching entre conducteurs et passagers.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public enum VehicleType {

    /** Véhicule de type berline classique. */
    SEDAN,

    /** Véhicule de type SUV. */
    SUV,

    /** Véhicule de type citadine compacte. */
    COMPACT,

    /** Véhicule de type break. */
    BREAK,

    /** Véhicule de type monospace. */
    MINIVAN,

    /** Véhicule électrique - facteur de réduction CO2 maximal. */
    ELECTRIC,

    /** Véhicule hybride - facteur de réduction CO2 élevé. */
    HYBRID
}
