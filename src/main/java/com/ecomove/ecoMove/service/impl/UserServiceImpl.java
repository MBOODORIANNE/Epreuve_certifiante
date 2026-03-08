package com.ecomove.ecoMove.service.impl;

import com.ecomove.ecoMove.dto.response.UserResponse;
import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.exception.ResourceNotFoundException;
import com.ecomove.ecoMove.mapper.EntityMapper;
import com.ecomove.ecoMove.repository.UserRepository;
import com.ecomove.ecoMove.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de gestion des utilisateurs.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
        return EntityMapper.toUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", email));
        return EntityMapper.toUserResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(EntityMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> getUsersByCompany(Long companyId) {
        return userRepository.findByCompanyIdAndActiveTrue(companyId).stream()
                .map(EntityMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public UserResponse updateProfile(Long id, String firstName, String lastName, String phone,
                                      String homeAddress, Double homeLatitude, Double homeLongitude) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));

        if (firstName != null) user.setFirstName(firstName);
        if (lastName != null) user.setLastName(lastName);
        if (phone != null) user.setPhone(phone);
        if (homeAddress != null) user.setHomeAddress(homeAddress);
        if (homeLatitude != null) user.setHomeLatitude(homeLatitude);
        if (homeLongitude != null) user.setHomeLongitude(homeLongitude);

        User updatedUser = userRepository.save(user);
        logger.info("Profil utilisateur mis à jour : {} (ID: {})", updatedUser.getEmail(), updatedUser.getId());

        return EntityMapper.toUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
        user.setActive(false);
        userRepository.save(user);
        logger.info("Compte utilisateur désactivé : {} (ID: {})", user.getEmail(), id);
    }

    @Override
    @Transactional
    public void activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", id));
        user.setActive(true);
        userRepository.save(user);
        logger.info("Compte utilisateur réactivé : {} (ID: {})", user.getEmail(), id);
    }
}
