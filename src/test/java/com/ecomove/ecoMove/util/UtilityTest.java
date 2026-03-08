package com.ecomove.ecoMove.util;

import com.ecomove.ecoMove.entity.enums.VehicleType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires des utilitaires de calcul CO2 et distance.
 *
 * @author MBONGO BOLLO Jarle
 * @version 1.0.0
 * @since 2026-03-06
 */
@DisplayName("Tests des utilitaires")
class UtilityTest {

    @Test
    @DisplayName("Calcul de distance Haversine - Paris à Lyon")
    void calculateDistance_ParisToLyon() {
        double distance = DistanceCalculator.calculateDistanceKm(48.8566, 2.3522, 45.7640, 4.8357);
        assertTrue(distance > 390 && distance < 500, "Distance Paris-Lyon ~ 392-462 km");
    }

    @Test
    @DisplayName("Calcul de distance - Même point")
    void calculateDistance_SamePoint() {
        double distance = DistanceCalculator.calculateDistanceKm(48.8566, 2.3522, 48.8566, 2.3522);
        assertEquals(0.0, distance);
    }

    @Test
    @DisplayName("Vérification de rayon - Dans le rayon")
    void isWithinRadius_True() {
        // Paris à Boulogne (~ 8 km)
        assertTrue(DistanceCalculator.isWithinRadius(48.8566, 2.3522, 48.8397, 2.2399, 15));
    }

    @Test
    @DisplayName("Vérification de rayon - Hors rayon")
    void isWithinRadius_False() {
        // Paris à Lyon (~ 392 km)
        assertFalse(DistanceCalculator.isWithinRadius(48.8566, 2.3522, 45.7640, 4.8357, 10));
    }

    @Test
    @DisplayName("Calcul CO2 économisé - Véhicule thermique")
    void calculateCO2Saved_Sedan() {
        double co2Saved = CarbonCalculator.calculateCO2Saved(100.0, 2, VehicleType.SEDAN);
        assertTrue(co2Saved > 0, "Le CO2 économisé doit être positif");
    }

    @Test
    @DisplayName("Calcul CO2 économisé - Véhicule électrique")
    void calculateCO2Saved_Electric() {
        double co2Sedan = CarbonCalculator.calculateCO2Saved(100.0, 2, VehicleType.SEDAN);
        double co2Electric = CarbonCalculator.calculateCO2Saved(100.0, 2, VehicleType.ELECTRIC);
        assertTrue(co2Electric > co2Sedan, "Un véhicule électrique doit économiser plus de CO2");
    }

    @Test
    @DisplayName("Calcul CO2 économisé - Distance nulle")
    void calculateCO2Saved_ZeroDistance() {
        double co2 = CarbonCalculator.calculateCO2Saved(0.0, 2, VehicleType.SEDAN);
        assertEquals(0.0, co2);
    }

    @Test
    @DisplayName("Calcul CO2 économisé - Aucun passager")
    void calculateCO2Saved_NoPassengers() {
        double co2 = CarbonCalculator.calculateCO2Saved(100.0, 0, VehicleType.SEDAN);
        assertEquals(0.0, co2);
    }

    @Test
    @DisplayName("Estimation économie financière")
    void estimateMoneySaved() {
        double saved = CarbonCalculator.estimateMoneySaved(100.0, 3);
        assertTrue(saved > 0);
        assertEquals(90.0, saved, "3 passagers x 0.30€/km x 100km = 90€");
    }
}
