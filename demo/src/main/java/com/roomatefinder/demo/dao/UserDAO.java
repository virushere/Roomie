package com.roomatefinder.demo.dao;

import com.roomatefinder.demo.models.UserEntity;
import java.util.List;
import java.util.Optional;

public interface UserDAO {
    UserEntity save(UserEntity user);
    Optional<UserEntity> findById(String id);
    UserEntity findByEmail(String email);
    UserEntity findByUsername(String username);
    List<UserEntity> findAll();
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    void deleteById(String id);
    List<UserEntity> findByRole(String role);
    List<UserEntity> findByIsActiveTrue();
}