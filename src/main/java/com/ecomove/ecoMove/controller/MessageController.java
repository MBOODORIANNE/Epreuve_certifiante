package com.ecomove.ecoMove.controller;

import com.ecomove.ecoMove.dto.request.MessageRequest;
import com.ecomove.ecoMove.dto.response.MessageResponse;
import com.ecomove.ecoMove.dto.response.UserResponse;
import com.ecomove.ecoMove.service.MessageService;
import com.ecomove.ecoMove.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la messagerie entre covoitureurs.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
@RestController
@RequestMapping("/api/messages")
@Tag(name = "Messagerie", description = "Système de messagerie intégrée entre covoitureurs")
@SecurityRequirement(name = "bearerAuth")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    public MessageController(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @PostMapping
    @Operation(summary = "Envoyer un message")
    public ResponseEntity<MessageResponse> sendMessage(Authentication authentication,
                                                        @Valid @RequestBody MessageRequest request) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return new ResponseEntity<>(messageService.sendMessage(user.getId(), request), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Récupérer tous mes messages")
    public ResponseEntity<List<MessageResponse>> getMyMessages(Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(messageService.getUserMessages(user.getId()));
    }

    @GetMapping("/conversation/{otherUserId}")
    @Operation(summary = "Récupérer la conversation avec un utilisateur")
    public ResponseEntity<List<MessageResponse>> getConversation(Authentication authentication,
                                                                   @PathVariable Long otherUserId) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(messageService.getConversation(user.getId(), otherUserId));
    }

    @GetMapping("/unread")
    @Operation(summary = "Récupérer les messages non lus")
    public ResponseEntity<List<MessageResponse>> getUnreadMessages(Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        return ResponseEntity.ok(messageService.getUnreadMessages(user.getId()));
    }

    @GetMapping("/unread/count")
    @Operation(summary = "Compter les messages non lus")
    public ResponseEntity<Map<String, Integer>> getUnreadCount(Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        int count = messageService.getUnreadCount(user.getId());
        return ResponseEntity.ok(Map.of("unreadCount", count));
    }

    @PutMapping("/{id}/read")
    @Operation(summary = "Marquer un message comme lu")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id, Authentication authentication) {
        UserResponse user = userService.getUserByEmail(authentication.getName());
        messageService.markAsRead(id, user.getId());
        return ResponseEntity.noContent().build();
    }
}
