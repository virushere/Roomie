package com.roomatefinder.demo.dao;

import com.roomatefinder.demo.models.UserPreferences;
import java.util.List;
import java.util.Optional;

public interface UserPreferencesDAO {
    UserPreferences save(UserPreferences preferences);
    Optional<UserPreferences> findById(Long id);
    Optional<UserPreferences> findByIdAndUserId(Long id, String userId);
    List<UserPreferences> findByUserId(String userId);
    void deleteById(Long id);
    List<UserPreferences> findAll();
}