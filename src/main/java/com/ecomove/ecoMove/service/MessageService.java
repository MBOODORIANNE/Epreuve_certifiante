package com.ecomove.ecoMove.service;

import com.ecomove.ecoMove.dto.request.MessageRequest;
import com.ecomove.ecoMove.dto.response.MessageResponse;
import java.util.List;

/**
 * Interface du service de messagerie intégrée.
 *
 * @author MBOO
 * @version 1.0.0
 * @since 2026-03-06
 */
public interface MessageService {

    MessageResponse sendMessage(Long senderId, MessageRequest request);

    List<MessageResponse> getConversation(Long userId1, Long userId2);

    List<MessageResponse> getUserMessages(Long userId);

    List<MessageResponse> getUnreadMessages(Long userId);

    void markAsRead(Long messageId, Long userId);

    int getUnreadCount(Long userId);
}
