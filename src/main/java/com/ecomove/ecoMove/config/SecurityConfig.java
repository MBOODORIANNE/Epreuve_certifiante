package com.ecomove.ecoMove.config;

import com.ecomove.ecoMove.security.CustomUserDetailsService;
import com.ecomove.ecoMove.security.JwtAuthenticationFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuration de sécurité de l'application EcoMove.
 * <p>
 * Implémente la sécurité basée sur JWT avec les caractéristiques suivantes :
 * <ul>
 *     <li>Authentification stateless via JWT</li>
 *     <li>Contrôle d'accès basé sur les rôles (RBAC)</li>
 *     <li>Hachage des mots de passe avec BCrypt</li>
 *     <li>Protection CSRF désactivée (API REST stateless)</li>
 *     <li>Endpoints publics pour l'authentification et la documentation Swagger</li>
 * </ul>
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Authentification JWT - Entrez le token sans le préfixe 'Bearer '"
)
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          CustomUserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Configure la chaîne de filtres de sécurité HTTP.
     *
     * @param http la configuration de sécurité HTTP
     * @return la chaîne de filtres configurée
     * @throws Exception en cas d'erreur de configuration
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Endpoints publics
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Endpoints administration EcoMove
                        .requestMatchers("/api/admin/**").hasRole("ECOMOVE_ADMIN")

                        // Endpoints entreprises
                        .requestMatchers("/api/companies/**").hasAnyRole("COMPANY_ADMIN", "ECOMOVE_ADMIN")

                        // Endpoints rapports carbone
                        .requestMatchers("/api/reports/**").hasAnyRole("COMPANY_ADMIN", "ECOMOVE_ADMIN")

                        // Tous les autres endpoints nécessitent une authentification
                        .anyRequest().authenticated()
                )
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configure le provider d'authentification avec le service utilisateur et l'encodeur de mots de passe.
     *
     * @return le provider d'authentification DAO
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * Fournit le gestionnaire d'authentification.
     *
     * @param config la configuration d'authentification
     * @return le gestionnaire d'authentification
     * @throws Exception en cas d'erreur
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Fournit l'encodeur de mots de passe BCrypt.
     *
     * @return l'encodeur BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
