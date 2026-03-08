package com.ecomove.ecoMove.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la création ou modification d'une entreprise partenaire.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public class CompanyRequest {

    @NotBlank(message = "Le nom de l'entreprise est obligatoire")
    private String name;

    @NotBlank(message = "Le numéro SIRET est obligatoire")
    @Size(min = 14, max = 14, message = "Le numéro SIRET doit contenir exactement 14 caractères")
    private String siret;

    @NotBlank(message = "L'adresse est obligatoire")
    private String address;

    private Double latitude;
    private Double longitude;

    @NotBlank(message = "L'adresse email est obligatoire")
    @Email(message = "L'adresse email doit être valide")
    private String email;

    private String phone;

    public CompanyRequest() {
    }

    public CompanyRequest(String name, String siret, String address, String email) {
        this.name = name;
        this.siret = siret;
        this.address = address;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSiret() {
        return siret;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
