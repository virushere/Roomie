//package com.roomatefinder.demo.repositories;
//
//import com.roomatefinder.demo.models.Message;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface MessageRepository extends JpaRepository<Message, Long> {
//
//    // Find all messages sent by a user
//    List<Message> findBySenderId(String senderId);
//
//    // Find all messages received by a user
//    List<Message> findByRecipientId(String recipientId);
//
//    // Find all unread messages for a recipient
//    List<Message> findByRecipientIdAndReadFalse(String recipientId);
//
//    // Find messages related to a specific room
//    List<Message> findByRoomId(Long roomId);
//
//    // Find conversation between two users (messages sent between them in either direction)
//    List<Message> findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderBySentAtDesc(
//            String user1Id, String user2Id, String user1IdAgain, String user2IdAgain);
//}