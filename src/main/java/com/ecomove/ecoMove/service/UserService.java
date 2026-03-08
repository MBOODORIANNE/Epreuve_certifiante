package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.response.UserResponse;
import java.util.List;

/**
 * Interface du service de gestion des utilisateurs.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public interface UserService {

    /**
     * Récupère un utilisateur par son identifiant.
     *
     * @param id l'identifiant de l'utilisateur
     * @return les informations de l'utilisateur
     */
    UserResponse getUserById(Long id);

    /**
     * Récupère un utilisateur par son adresse email.
     *
     * @param email l'adresse email
     * @return les informations de l'utilisateur
     */
    UserResponse getUserByEmail(String email);

    /**
     * Récupère tous les utilisateurs.
     *
     * @return la liste de tous les utilisateurs
     */
    List<UserResponse> getAllUsers();

    /**
     * Récupère les employés d'une entreprise.
     *
     * @param companyId l'identifiant de l'entreprise
     * @return la liste des employés
     */
    List<UserResponse> getUsersByCompany(Long companyId);

    /**
     * Met à jour le profil d'un utilisateur.
     *
     * @param id        l'identifiant de l'utilisateur
     * @param firstName le nouveau prénom
     * @param lastName  le nouveau nom
     * @param phone     le nouveau numéro de téléphone
     * @param homeAddress la nouvelle adresse
     * @param homeLatitude la nouvelle latitude
     * @param homeLongitude la nouvelle longitude
     * @return les informations mises à jour
     */
    UserResponse updateProfile(Long id, String firstName, String lastName, String phone,
                               String homeAddress, Double homeLatitude, Double homeLongitude);

    /**
     * Désactive un compte utilisateur.
     *
     * @param id l'identifiant de l'utilisateur
     */
    void deactivateUser(Long id);

    /**
     * Réactive un compte utilisateur.
     *
     * @param id l'identifiant de l'utilisateur
     */
    void activateUser(Long id);
}
