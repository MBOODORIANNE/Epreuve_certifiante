package com.ecomove.ecoMove.service.impl;

import com.ecomove.ecoMove.dto.request.LoginRequest;
import com.ecomove.ecoMove.dto.request.RegisterRequest;
import com.ecomove.ecoMove.dto.response.AuthResponse;
import com.ecomove.ecoMove.entity.Company;
import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.entity.enums.Role;
import com.ecomove.ecoMove.exception.BadRequestException;
import com.ecomove.ecoMove.exception.ResourceNotFoundException;
import com.ecomove.ecoMove.repository.CompanyRepository;
import com.ecomove.ecoMove.repository.UserRepository;
import com.ecomove.ecoMove.security.JwtTokenProvider;
import com.ecomove.ecoMove.service.AuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implémentation du service d'authentification.
 * <p>
 * Gère l'inscription des utilisateurs et l'authentification via JWT.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository, CompanyRepository companyRepository,
                           PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        logger.info("Tentative d'inscription pour l'email : {}", request.getEmail());

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Un compte avec cet email existe déjà : " + request.getEmail());
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setHomeAddress(request.getHomeAddress());
        user.setHomeLatitude(request.getHomeLatitude());
        user.setHomeLongitude(request.getHomeLongitude());
        user.setRole(Role.EMPLOYEE);

        if (request.getCompanyId() != null) {
            Company company = companyRepository.findById(request.getCompanyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Entreprise", "id", request.getCompanyId()));
            user.setCompany(company);
        }

        User savedUser = userRepository.save(user);
        logger.info("Utilisateur inscrit avec succès : {} (ID: {})", savedUser.getEmail(), savedUser.getId());

        String token = jwtTokenProvider.generateTokenFromEmail(savedUser.getEmail());

        return new AuthResponse(token, savedUser.getId(), savedUser.getEmail(),
                savedUser.getFullName(), savedUser.getRole().name());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        logger.info("Tentative de connexion pour l'email : {}", request.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "email", request.getEmail()));

        String token = jwtTokenProvider.generateToken(authentication);

        logger.info("Connexion réussie pour l'utilisateur : {} (Rôle: {})", user.getEmail(), user.getRole());

        return new AuthResponse(token, user.getId(), user.getEmail(),
                user.getFullName(), user.getRole().name());
    }
}
