package com.ecomove.ecoMove.util;

import com.ecomove.ecoMove.entity.enums.VehicleType;

/**
 * Utilitaire pour le calcul de l'empreinte carbone économisée par le covoiturage.
 * <p>
 * Les calculs sont basés sur les données moyennes d'émission de CO2 par km
 * selon le type de véhicule et le nombre de passagers en covoiturage.
 * </p>
 * <p>
 * Source de référence : ADEME (Agence de l'Environnement et de la Maîtrise de l'Énergie).
 * </p>
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public final class CarbonCalculator {

    /** Émission moyenne d'un véhicule thermique en kg CO2/km. */
    private static final double AVERAGE_CO2_PER_KM = 0.120;

    /** Émission d'un véhicule électrique en kg CO2/km (production d'électricité incluse). */
    private static final double ELECTRIC_CO2_PER_KM = 0.020;

    /** Émission d'un véhicule hybride en kg CO2/km. */
    private static final double HYBRID_CO2_PER_KM = 0.070;

    private CarbonCalculator() {
        // Classe utilitaire - pas d'instanciation
    }

    /**
     * Calcule le CO2 économisé (en kg) pour un trajet en covoiturage.
     * <p>
     * Le calcul compare les émissions si chaque passager avait conduit seul
     * versus le partage d'un seul véhicule.
     * </p>
     *
     * @param distanceKm    la distance du trajet en kilomètres
     * @param passengers    le nombre de passagers (hors conducteur)
     * @param vehicleType   le type du véhicule utilisé
     * @return le CO2 économisé en kilogrammes
     */
    public static double calculateCO2Saved(double distanceKm, int passengers, VehicleType vehicleType) {
        if (distanceKm <= 0 || passengers <= 0) {
            return 0.0;
        }

        double co2PerKm = getCO2PerKm(vehicleType);

        // CO2 si chaque passager roulait seul
        double co2WithoutCarpooling = (passengers + 1) * AVERAGE_CO2_PER_KM * distanceKm;

        // CO2 avec covoiturage (un seul véhicule)
        double co2WithCarpooling = co2PerKm * distanceKm;

        return Math.round((co2WithoutCarpooling - co2WithCarpooling) * 100.0) / 100.0;
    }

    /**
     * Retourne le facteur d'émission CO2 par km selon le type de véhicule.
     *
     * @param vehicleType le type de véhicule
     * @return les émissions en kg CO2/km
     */
    public static double getCO2PerKm(VehicleType vehicleType) {
        if (vehicleType == null) {
            return AVERAGE_CO2_PER_KM;
        }
        switch (vehicleType) {
            case ELECTRIC:
                return ELECTRIC_CO2_PER_KM;
            case HYBRID:
                return HYBRID_CO2_PER_KM;
            default:
                return AVERAGE_CO2_PER_KM;
        }
    }

    /**
     * Estime l'économie financière pour un trajet en covoiturage.
     *
     * @param distanceKm  la distance en km
     * @param passengers  le nombre de passagers
     * @return l'économie estimée en euros
     */
    public static double estimateMoneySaved(double distanceKm, int passengers) {
        // Coût moyen par km en voiture solo : 0.30€ (carburant + usure)
        double costPerKm = 0.30;
        return Math.round(passengers * costPerKm * distanceKm * 100.0) / 100.0;
    }
}
