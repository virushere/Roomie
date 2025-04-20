package com.roomatefinder.demo.dao.impl;

import com.roomatefinder.demo.dao.MessageDAO;
import com.roomatefinder.demo.models.Message;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MessageDAOImpl implements MessageDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public MessageDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public Message save(Message message) {
        getCurrentSession().saveOrUpdate(message);
        return message;
    }

    @Override
    public Optional<Message> findById(Long id) {
        Message message = getCurrentSession().get(Message.class, id);
        return Optional.ofNullable(message);
    }

    @Override
    public List<Message> findBySenderId(String senderId) {
        Query<Message> query = getCurrentSession().createQuery(
                "FROM Message WHERE senderId = :senderId", Message.class);
        query.setParameter("senderId", senderId);
        return query.list();
    }

    @Override
    public List<Message> findByRecipientId(String recipientId) {
        Query<Message> query = getCurrentSession().createQuery(
                "FROM Message WHERE recipientId = :recipientId", Message.class);
        query.setParameter("recipientId", recipientId);
        return query.list();
    }

    @Override
    public List<Message> findByRecipientIdAndReadFalse(String recipientId) {
        Query<Message> query = getCurrentSession().createQuery(
                "FROM Message WHERE recipientId = :recipientId AND read = false", Message.class);
        query.setParameter("recipientId", recipientId);
        return query.list();
    }

    @Override
    public List<Message> findByRoomId(Long roomId) {
        Query<Message> query = getCurrentSession().createQuery(
                "FROM Message WHERE roomId = :roomId", Message.class);
        query.setParameter("roomId", roomId);
        return query.list();
    }

    @Override
    public List<Message> findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderBySentAtDesc(
            String user1Id, String user2Id, String user1IdAgain, String user2IdAgain) {
        Query<Message> query = getCurrentSession().createQuery(
                "FROM Message WHERE (senderId = :user1Id AND recipientId = :user2Id) OR " +
                        "(senderId = :user2Id AND recipientId = :user1Id) " +
                        "ORDER BY sentAt DESC", Message.class);
        query.setParameter("user1Id", user1Id);
        query.setParameter("user2Id", user2Id);
        return query.list();
    }
}