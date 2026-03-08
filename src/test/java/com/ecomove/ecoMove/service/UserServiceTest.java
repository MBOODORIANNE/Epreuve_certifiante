package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.response.UserResponse;
import com.ecomove.ecoMove.entity.Company;
import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.entity.enums.Role;
import com.ecomove.ecoMove.exception.ResourceNotFoundException;
import com.ecomove.ecoMove.repository.UserRepository;
import com.ecomove.ecoMove.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitaires du service de gestion des utilisateurs.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Tests du UserService")
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private Company testCompany;

    @BeforeEach
    void setUp() {
        testCompany = new Company("EcoMove Corp", "12345678901234", "123 Rue Verte", "contact@ecomove.com");
        testCompany.setId(1L);
        testCompany.setCreatedAt(LocalDateTime.now());

        testUser = new User("jarle@ecomove.com", "hashedPassword", "Jarle", "MBONGO", Role.EMPLOYEE);
        testUser.setId(1L);
        testUser.setPhone("0612345678");
        testUser.setHomeAddress("10 Rue de la Paix, Paris");
        testUser.setHomeLatitude(48.8566);
        testUser.setHomeLongitude(2.3522);
        testUser.setActive(true);
        testUser.setCompany(testCompany);
        testUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Récupérer un utilisateur par ID - Succès")
    void getUserById_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UserResponse result = userService.getUserById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("jarle@ecomove.com", result.getEmail());
        assertEquals("Jarle", result.getFirstName());
        assertEquals("MBONGO", result.getLastName());
        assertEquals("EMPLOYEE", result.getRole());
        assertEquals("EcoMove Corp", result.getCompanyName());
        assertTrue(result.isActive());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Récupérer un utilisateur par ID - Non trouvé")
    void getUserById_NotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.getUserById(999L));
        verify(userRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Récupérer un utilisateur par email - Succès")
    void getUserByEmail_Success() {
        when(userRepository.findByEmail("jarle@ecomove.com")).thenReturn(Optional.of(testUser));

        UserResponse result = userService.getUserByEmail("jarle@ecomove.com");

        assertNotNull(result);
        assertEquals("jarle@ecomove.com", result.getEmail());
        verify(userRepository, times(1)).findByEmail("jarle@ecomove.com");
    }

    @Test
    @DisplayName("Récupérer tous les utilisateurs")
    void getAllUsers_Success() {
        User user2 = new User("alice@ecomove.com", "pass", "Alice", "Dupont", Role.EMPLOYEE);
        user2.setId(2L);
        user2.setCreatedAt(LocalDateTime.now());

        when(userRepository.findAll()).thenReturn(Arrays.asList(testUser, user2));

        List<UserResponse> results = userService.getAllUsers();

        assertNotNull(results);
        assertEquals(2, results.size());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Récupérer les employés d'une entreprise")
    void getUsersByCompany_Success() {
        when(userRepository.findByCompanyIdAndActiveTrue(1L)).thenReturn(Arrays.asList(testUser));

        List<UserResponse> results = userService.getUsersByCompany(1L);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals("Jarle", results.get(0).getFirstName());
        verify(userRepository, times(1)).findByCompanyIdAndActiveTrue(1L);
    }

    @Test
    @DisplayName("Mettre à jour le profil utilisateur")
    void updateProfile_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        UserResponse result = userService.updateProfile(1L, "JarleUpdated", "MBONGOUpdated",
                "0699999999", "20 Rue Neuve", 48.86, 2.35);

        assertNotNull(result);
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Désactiver un compte utilisateur")
    void deactivateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        assertDoesNotThrow(() -> userService.deactivateUser(1L));
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Réactiver un compte utilisateur")
    void activateUser_Success() {
        testUser.setActive(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        assertDoesNotThrow(() -> userService.activateUser(1L));
        verify(userRepository, times(1)).save(any(User.class));
    }
}
