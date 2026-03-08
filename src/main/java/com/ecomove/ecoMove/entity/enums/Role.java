package com.ecomove.ecoMove.entity.enums;

/**
 * Énumération représentant les rôles disponibles dans le système EcoMove.
 * <p>
 * Le système utilise un contrôle d'accès basé sur les rôles (RBAC) pour gérer
 * les permissions des différents types d'utilisateurs.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public enum Role {

    /**
     * Rôle attribué aux employés des entreprises partenaires.
     * Permet d'accéder aux fonctionnalités de covoiturage.
     */
    EMPLOYEE,

    /**
     * Rôle attribué aux administrateurs des entreprises partenaires.
     * Permet d'accéder aux rapports et à la gestion des employés de l'entreprise.
     */
    COMPANY_ADMIN,

    /**
     * Rôle attribué aux administrateurs de la plateforme EcoMove.
     * Accès complet à toutes les fonctionnalités de la plateforme.
     */
    ECOMOVE_ADMIN
}
