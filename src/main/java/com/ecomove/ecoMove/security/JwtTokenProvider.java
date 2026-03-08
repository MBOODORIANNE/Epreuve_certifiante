package com.ecomove.ecoMove.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Fournisseur de tokens JWT pour l'authentification.
 * <p>
 * Gère la génération, la validation et l'extraction des informations
 * depuis les tokens JWT utilisés pour sécuriser l'API REST.
 * </p>
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    @Value("${app.jwt.expiration-ms}")
    private long jwtExpirationMs;

    /**
     * Génère la clé secrète HMAC-SHA à partir de la configuration.
     *
     * @return la clé secrète
     */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Génère un token JWT à partir de l'authentification.
     *
     * @param authentication l'objet d'authentification Spring Security
     * @return le token JWT généré
     */
    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return generateTokenFromEmail(userDetails.getUsername());
    }

    /**
     * Génère un token JWT à partir d'une adresse email.
     *
     * @param email l'adresse email de l'utilisateur
     * @return le token JWT généré
     */
    public String generateTokenFromEmail(String email) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .subject(email)
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    /**
     * Extrait l'adresse email depuis un token JWT.
     *
     * @param token le token JWT
     * @return l'adresse email contenue dans le token
     */
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    /**
     * Valide un token JWT.
     *
     * @param token le token JWT à valider
     * @return true si le token est valide, false sinon
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Token JWT malformé : {}", ex.getMessage());
        } catch (ExpiredJwtException ex) {
            logger.error("Token JWT expiré : {}", ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            logger.error("Token JWT non supporté : {}", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            logger.error("Le token JWT est vide : {}", ex.getMessage());
        } catch (SecurityException ex) {
            logger.error("Signature JWT invalide : {}", ex.getMessage());
        }
        return false;
    }
}
