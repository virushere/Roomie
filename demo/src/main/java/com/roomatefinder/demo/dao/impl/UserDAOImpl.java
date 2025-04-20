package com.roomatefinder.demo.dao.impl;

import com.roomatefinder.demo.dao.UserDAO;
import com.roomatefinder.demo.models.UserEntity;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public UserEntity save(UserEntity user) {
        getCurrentSession().saveOrUpdate(user);
        return user;
    }

    @Override
    public Optional<UserEntity> findById(String id) {
        UserEntity user = getCurrentSession().get(UserEntity.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        Query<UserEntity> query = getCurrentSession().createQuery(
                "FROM UserEntity WHERE email = :email", UserEntity.class);
        query.setParameter("email", email);
        return query.uniqueResult();
    }

    @Override
    public UserEntity findByUsername(String username) {
        Query<UserEntity> query = getCurrentSession().createQuery(
                "FROM UserEntity WHERE username = :username", UserEntity.class);
        query.setParameter("username", username);
        return query.uniqueResult();
    }

    @Override
    public List<UserEntity> findAll() {
        return getCurrentSession().createQuery("FROM UserEntity", UserEntity.class).list();
    }

    @Override
    public boolean existsByEmail(String email) {
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(u) FROM UserEntity u WHERE u.email = :email", Long.class);
        query.setParameter("email", email);
        return query.uniqueResult() > 0;
    }

    @Override
    public boolean existsByUsername(String username) {
        Query<Long> query = getCurrentSession().createQuery(
                "SELECT COUNT(u) FROM UserEntity u WHERE u.username = :username", Long.class);
        query.setParameter("username", username);
        return query.uniqueResult() > 0;
    }

    @Override
    public void deleteById(String id) {
        UserEntity user = getCurrentSession().get(UserEntity.class, id);
        if (user != null) {
            getCurrentSession().delete(user);
        }
    }

    @Override
    public List<UserEntity> findByRole(String role) {
        Query<UserEntity> query = getCurrentSession().createQuery(
                "FROM UserEntity WHERE userType = :role", UserEntity.class);
        query.setParameter("role", role);
        return query.list();
    }

    @Override
    public List<UserEntity> findByIsActiveTrue() {
        Query<UserEntity> query = getCurrentSession().createQuery(
                "FROM UserEntity WHERE isActive = true", UserEntity.class);
        return query.list();
    }
}