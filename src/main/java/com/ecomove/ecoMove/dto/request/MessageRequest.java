package com.ecomove.ecoMove.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO pour l'envoi d'un message entre utilisateurs.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
public class MessageRequest {

    @NotNull(message = "L'identifiant du destinataire est obligatoire")
    private Long receiverId;

    @NotBlank(message = "Le contenu du message est obligatoire")
    private String content;

    private Long tripId;

    public MessageRequest() {
    }

    public MessageRequest(Long receiverId, String content) {
        this.receiverId = receiverId;
        this.content = content;
    }

    public Long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTripId() {
        return tripId;
    }

    public void setTripId(Long tripId) {
        this.tripId = tripId;
    }
}
