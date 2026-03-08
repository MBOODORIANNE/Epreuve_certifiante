package com.ecomove.ecoMove;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Classe principale de l'application EcoMove.
 * <p>
 * EcoMove est une plateforme B2B de covoiturage domicile-travail destinée aux entreprises partenaires.
 * L'objectif est de réduire l'empreinte carbone des trajets professionnels, d'optimiser les déplacements
 * domicile-travail et de fournir aux entreprises des indicateurs de performance environnementale.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@SpringBootApplication
@EnableScheduling
public class EcoMoveApplication {

    /**
     * Point d'entrée principal de l'application EcoMove.
     *
     * @param args les arguments de la ligne de commande
     */
    public static void main(String[] args) {
        SpringApplication.run(EcoMoveApplication.class, args);
    }
}
