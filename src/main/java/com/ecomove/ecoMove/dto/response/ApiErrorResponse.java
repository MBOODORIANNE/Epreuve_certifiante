package com.ecomove.ecoMove.dto.response;

import java.time.LocalDateTime;

/**
 * DTO de réponse standard pour les erreurs API.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public class ApiErrorResponse {

    private int status;
    private String message;
    private String details;
    private LocalDateTime timestamp;

    public ApiErrorResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiErrorResponse(int status, String message, String details) {
        this.status = status;
        this.message = message;
        this.details = details;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
