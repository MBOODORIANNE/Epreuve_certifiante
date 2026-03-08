package com.ecomove.ecoMove.exception;

/**
 * Exception levée lors d'un accès non autorisé à une ressource.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(String message) {
        super(message);
    }
}
