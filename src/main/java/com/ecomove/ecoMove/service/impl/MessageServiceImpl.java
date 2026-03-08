package com.ecomove.ecoMove.service.impl;

import com.ecomove.ecoMove.dto.request.MessageRequest;
import com.ecomove.ecoMove.dto.response.MessageResponse;
import com.ecomove.ecoMove.entity.Message;
import com.ecomove.ecoMove.entity.Trip;
import com.ecomove.ecoMove.entity.User;
import com.ecomove.ecoMove.exception.ResourceNotFoundException;
import com.ecomove.ecoMove.exception.UnauthorizedException;
import com.ecomove.ecoMove.mapper.EntityMapper;
import com.ecomove.ecoMove.repository.MessageRepository;
import com.ecomove.ecoMove.repository.TripRepository;
import com.ecomove.ecoMove.repository.UserRepository;
import com.ecomove.ecoMove.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implémentation du service de messagerie intégrée.
 *
 * @author MBO0
 * @version 1.0.0
 * @since 2026-03-06
 */
@Service
public class MessageServiceImpl implements MessageService {

    private static final Logger logger = LoggerFactory.getLogger(MessageServiceImpl.class);

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository,
                              TripRepository tripRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
    }

    @Override
    @Transactional
    public MessageResponse sendMessage(Long senderId, MessageRequest request) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", senderId));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur", "id", request.getReceiverId()));

        Message message = new Message(sender, receiver, request.getContent());

        if (request.getTripId() != null) {
            Trip trip = tripRepository.findById(request.getTripId())
                    .orElseThrow(() -> new ResourceNotFoundException("Trajet", "id", request.getTripId()));
            message.setTrip(trip);
        }

        Message savedMessage = messageRepository.save(message);
        logger.info("Message envoyé - De: {} À: {}", sender.getFullName(), receiver.getFullName());

        return EntityMapper.toMessageResponse(savedMessage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getConversation(Long userId1, Long userId2) {
        return messageRepository.findConversation(userId1, userId2).stream()
                .map(EntityMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getUserMessages(Long userId) {
        return messageRepository.findBySenderIdOrReceiverIdOrderByCreatedAtDesc(userId, userId).stream()
                .map(EntityMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponse> getUnreadMessages(Long userId) {
        return messageRepository.findByReceiverIdAndReadFalse(userId).stream()
                .map(EntityMapper::toMessageResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message", "id", messageId));

        if (!message.getReceiver().getId().equals(userId)) {
            throw new UnauthorizedException("Seul le destinataire peut marquer ce message comme lu");
        }

        message.setRead(true);
        messageRepository.save(message);
    }

    @Override
    @Transactional(readOnly = true)
    public int getUnreadCount(Long userId) {
        return messageRepository.countByReceiverIdAndReadFalse(userId);
    }
}
