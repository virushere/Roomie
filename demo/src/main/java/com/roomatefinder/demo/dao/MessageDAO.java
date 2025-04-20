package com.roomatefinder.demo.dao;

import com.roomatefinder.demo.models.Message;
import java.util.List;
import java.util.Optional;

public interface MessageDAO {
    Message save(Message message);
    Optional<Message> findById(Long id);
    List<Message> findBySenderId(String senderId);
    List<Message> findByRecipientId(String recipientId);
    List<Message> findByRecipientIdAndReadFalse(String recipientId);
    List<Message> findByRoomId(Long roomId);
    List<Message> findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderBySentAtDesc(
            String user1Id, String user2Id, String user1IdAgain, String user2IdAgain);
}