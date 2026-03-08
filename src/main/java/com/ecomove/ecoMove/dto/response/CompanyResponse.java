package com.ecomove.ecoMove.dto.response;

import java.time.LocalDateTime;

/**
 * DTO de réponse contenant les informations d'une entreprise partenaire.
 *
 * @author MBO
 * @version 1.0.0
 * @since 2026-03-06
 */
public class CompanyResponse {

    private Long id;
    private String name;
    private String siret;
    private String address;
    private Double latitude;
    private Double longitude;
    private String email;
    private String phone;
    private boolean active;
    private int employeeCount;
    private LocalDateTime createdAt;

    public CompanyResponse() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSiret() { return siret; }
    public void setSiret(String siret) { this.siret = siret; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }

    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public int getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(int employeeCount) { this.employeeCount = employeeCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
