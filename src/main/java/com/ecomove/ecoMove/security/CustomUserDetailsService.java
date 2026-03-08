package com.ecomove.ecoMove.security;

import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Implémentation personnalisée de {@link UserDetailsService} pour Spring Security.
 * <p>
 * Charge les informations de l'utilisateur depuis la base de données
 * pour l'authentification et l'autorisation.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par son adresse email.
     *
     * @param email l'adresse email de l'utilisateur
     * @return les détails de l'utilisateur pour Spring Security
     * @throws UsernameNotFoundException si l'utilisateur est introuvable ou inactif
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.warn("Tentative de connexion avec un email inconnu : {}", email);
                    return new UsernameNotFoundException("Utilisateur introuvable avec l'email : " + email);
                });

        if (!user.isActive()) {
            logger.warn("Tentative de connexion avec un compte désactivé : {}", email);
            throw new UsernameNotFoundException("Le compte de l'utilisateur est désactivé : " + email);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.isActive(),
                true, true, true,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}
