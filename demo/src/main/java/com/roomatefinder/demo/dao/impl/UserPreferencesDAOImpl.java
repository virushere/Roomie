package com.roomatefinder.demo.dao.impl;

import com.roomatefinder.demo.dao.UserPreferencesDAO;
import com.roomatefinder.demo.models.UserPreferences;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserPreferencesDAOImpl implements UserPreferencesDAO {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserPreferencesDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    private Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    @Override
    public UserPreferences save(UserPreferences preferences) {
        getCurrentSession().saveOrUpdate(preferences);
        return preferences;
    }

    @Override
    public Optional<UserPreferences> findById(Long id) {
        UserPreferences preferences = getCurrentSession().get(UserPreferences.class, id);
        return Optional.ofNullable(preferences);
    }

    @Override
    public Optional<UserPreferences> findByIdAndUserId(Long id, String userId) {
        Query<UserPreferences> query = getCurrentSession().createQuery(
                "FROM UserPreferences WHERE id = :id AND userId = :userId", UserPreferences.class);
        query.setParameter("id", id);
        query.setParameter("userId", userId);
        UserPreferences result = query.uniqueResult();
        return Optional.ofNullable(result);
    }

    @Override
    public List<UserPreferences> findByUserId(String userId) {
        return getCurrentSession().createQuery(
                        "FROM UserPreferences up " +
                                "LEFT JOIN FETCH up.preferredAreas " +
                                "LEFT JOIN FETCH up.languagesKnown " +
                                "WHERE up.userId = :userId",
                        UserPreferences.class)
                .setParameter("userId", userId)
                .list();
    }

    @Override
    public void deleteById(Long id) {
        UserPreferences preferences = getCurrentSession().get(UserPreferences.class, id);
        if (preferences != null) {
            getCurrentSession().delete(preferences);
        }
    }

    @Override
    public List<UserPreferences> findAll() {
        return getCurrentSession().createQuery(
                        "SELECT DISTINCT up FROM UserPreferences up " +
                                "LEFT JOIN FETCH up.preferredAreas " +
                                "LEFT JOIN FETCH up.languagesKnown",
                        UserPreferences.class)
                .list();
    }
}