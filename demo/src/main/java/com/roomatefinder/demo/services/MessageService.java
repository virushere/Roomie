package com.roomatefinder.demo.services;

import com.roomatefinder.demo.dao.MessageDAO;
import com.roomatefinder.demo.models.Message;
import com.roomatefinder.demo.models.RoomInfo;
import com.roomatefinder.demo.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService {

    private final MessageDAO messageDAO;
    private final UserService userService;
    private final RoomInfoService roomInfoService;

    @Autowired
    public MessageService(MessageDAO messageDAO, UserService userService, RoomInfoService roomInfoService) {
        this.messageDAO = messageDAO;
        this.userService = userService;
        this.roomInfoService = roomInfoService;
    }

    // Find message by ID
    public Optional<Message> findById(Long id) {
        return messageDAO.findById(id);
    }

    // Save a new message
    public Message saveMessage(Message message) {
        return messageDAO.save(message);
    }

    // Get all messages sent by a user
    public List<Message> getSentMessages(String userId) {
        List<Message> messages = messageDAO.findBySenderId(userId);

        // Populate recipient information
        messages.forEach(message -> {
            UserEntity recipient = userService.getUserById(message.getRecipientId());
            message.setRecipient(recipient);

            if (message.getRoomId() != null) {
                RoomInfo room = roomInfoService.getRoomById(message.getRoomId());
                message.setRoom(room);
            }
        });

        return messages;
    }

    // Get all messages received by a user
    public List<Message> getReceivedMessages(String userId) {
        List<Message> messages = messageDAO.findByRecipientId(userId);

        // Populate sender information
        messages.forEach(message -> {
            UserEntity sender = userService.getUserById(message.getSenderId());
            message.setSender(sender);

            if (message.getRoomId() != null) {
                RoomInfo room = roomInfoService.getRoomById(message.getRoomId());
                message.setRoom(room);
            }
        });

        return messages;
    }

    // Mark a message as read
    public void markAsRead(Long messageId) {
        messageDAO.findById(messageId)
                .ifPresent(message -> {
                    message.setRead(true);
                    messageDAO.save(message);
                });
    }

    // Mark all messages from a sender to a recipient as read
    public void markAllAsRead(String senderId, String recipientId) {
        List<Message> messages = messageDAO.findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderBySentAtDesc(
                senderId, recipientId, recipientId, senderId);

        messages.stream()
                .filter(message -> message.getRecipientId().equals(recipientId) && !message.getRead())
                .forEach(message -> {
                    message.setRead(true);
                    messageDAO.save(message);
                });
    }

    // Get conversation between two users
    public List<Message> getConversation(String user1Id, String user2Id) {
        List<Message> messages = messageDAO.findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderBySentAtDesc(
                user1Id, user2Id, user1Id, user2Id);

        // Populate user information
        messages.forEach(message -> {
            UserEntity sender = userService.getUserById(message.getSenderId());
            UserEntity recipient = userService.getUserById(message.getRecipientId());

            message.setSender(sender);
            message.setRecipient(recipient);

            if (message.getRoomId() != null) {
                RoomInfo room = roomInfoService.getRoomById(message.getRoomId());
                message.setRoom(room);
            }
        });

        return messages;
    }

    // Count unread messages for a user
    public int countUnreadMessages(String userId) {
        return messageDAO.findByRecipientIdAndReadFalse(userId).size();
    }
}