package com.ecomove.ecoMove.util;

/**
 * Utilitaire pour le calcul de distances géographiques entre deux points GPS.
 * <p>
 * Utilise la formule de Haversine pour calculer la distance orthodromique
 * (distance à vol d'oiseau) entre deux coordonnées GPS.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public final class DistanceCalculator {

    /** Rayon moyen de la Terre en kilomètres. */
    private static final double EARTH_RADIUS_KM = 6371.0;

    private DistanceCalculator() {
        // Classe utilitaire - pas d'instanciation
    }

    /**
     * Calcule la distance en kilomètres entre deux points GPS
     * en utilisant la formule de Haversine.
     *
     * @param lat1 latitude du premier point
     * @param lng1 longitude du premier point
     * @param lat2 latitude du second point
     * @param lng2 longitude du second point
     * @return la distance en kilomètres, arrondie à 2 décimales
     */
    public static double calculateDistanceKm(double lat1, double lng1, double lat2, double lng2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLng / 2) * Math.sin(dLng / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c;

        return Math.round(distance * 100.0) / 100.0;
    }

    /**
     * Vérifie si deux points sont dans un rayon donné l'un de l'autre.
     *
     * @param lat1     latitude du premier point
     * @param lng1     longitude du premier point
     * @param lat2     latitude du second point
     * @param lng2     longitude du second point
     * @param radiusKm le rayon en kilomètres
     * @return true si les deux points sont dans le rayon, false sinon
     */
    public static boolean isWithinRadius(double lat1, double lng1, double lat2, double lng2, double radiusKm) {
        return calculateDistanceKm(lat1, lng1, lat2, lng2) <= radiusKm;
    }
}
