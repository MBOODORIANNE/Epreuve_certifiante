package com.ecomove.ecoMove.exception;

/**
 * Exception levée lors d'une requête invalide ou d'une violation de règle métier.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}
