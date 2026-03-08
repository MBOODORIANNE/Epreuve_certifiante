package com.ecomove.ecoMove.entity;

import com.ecomove.ecoMove.entity.enums.Role;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Entité représentant un utilisateur de la plateforme EcoMove.
 * <p>
 * Un utilisateur peut être un employé d'une entreprise partenaire,
 * un administrateur d'entreprise ou un administrateur EcoMove.
 * Les employés peuvent être à la fois conducteurs et passagers.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Adresse email unique servant d'identifiant de connexion. */
    @Column(nullable = false, unique = true)
    private String email;

    /** Mot de passe hashé avec BCrypt. */
    @Column(nullable = false)
    private String password;

    /** Prénom de l'utilisateur. */
    @Column(nullable = false)
    private String firstName;

    /** Nom de famille de l'utilisateur. */
    @Column(nullable = false)
    private String lastName;

    /** Numéro de téléphone de l'utilisateur. */
    private String phone;

    /** Adresse du domicile de l'utilisateur. */
    private String homeAddress;

    /** Latitude GPS du domicile. */
    private Double homeLatitude;

    /** Longitude GPS du domicile. */
    private Double homeLongitude;

    /** Rôle de l'utilisateur dans le système (RBAC). */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /** Entreprise partenaire à laquelle l'utilisateur est rattaché. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    /** Indique si le compte utilisateur est actif. */
    @Column(nullable = false)
    private boolean active = true;

    /** URL de la photo de profil de l'utilisateur. */
    private String profilePictureUrl;

    /** Préférence de trajet : fumeur ou non. */
    @Column(nullable = false)
    private boolean smokingAllowed = false;

    /** Préférence de trajet : musique autorisée ou non. */
    @Column(nullable = false)
    private boolean musicAllowed = true;

    /** Préférence de trajet : animaux autorisés ou non. */
    @Column(nullable = false)
    private boolean petsAllowed = false;

    /** Liste des véhicules possédés par l'utilisateur. */
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Vehicle> vehicles = new ArrayList<>();

    /** Liste des trajets proposés en tant que conducteur. */
    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Trip> tripsAsDriver = new ArrayList<>();

    /** Liste des réservations effectuées en tant que passager. */
    @OneToMany(mappedBy = "passenger", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Booking> bookings = new ArrayList<>();

    /** Date de création du compte. */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Date de la dernière mise à jour du profil. */
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    /** Constructeur par défaut requis par JPA. */
    public User() {
    }

    /**
     * Constructeur avec les champs obligatoires.
     *
     * @param email     l'adresse email
     * @param password  le mot de passe (doit être hashé avant stockage)
     * @param firstName le prénom
     * @param lastName  le nom de famille
     * @param role      le rôle de l'utilisateur
     */
    public User(String email, String password, String firstName, String lastName, Role role) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    // ==================== Getters & Setters ====================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public Double getHomeLatitude() {
        return homeLatitude;
    }

    public void setHomeLatitude(Double homeLatitude) {
        this.homeLatitude = homeLatitude;
    }

    public Double getHomeLongitude() {
        return homeLongitude;
    }

    public void setHomeLongitude(Double homeLongitude) {
        this.homeLongitude = homeLongitude;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public boolean isSmokingAllowed() {
        return smokingAllowed;
    }

    public void setSmokingAllowed(boolean smokingAllowed) {
        this.smokingAllowed = smokingAllowed;
    }

    public boolean isMusicAllowed() {
        return musicAllowed;
    }

    public void setMusicAllowed(boolean musicAllowed) {
        this.musicAllowed = musicAllowed;
    }

    public boolean isPetsAllowed() {
        return petsAllowed;
    }

    public void setPetsAllowed(boolean petsAllowed) {
        this.petsAllowed = petsAllowed;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }

    public List<Trip> getTripsAsDriver() {
        return tripsAsDriver;
    }

    public void setTripsAsDriver(List<Trip> tripsAsDriver) {
        this.tripsAsDriver = tripsAsDriver;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Retourne le nom complet de l'utilisateur.
     *
     * @return le prénom et le nom concaténés
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    // ==================== equals, hashCode, toString ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", role=" + role +
                ", active=" + active +
                '}';
    }
}
