package com.roomatefinder.demo.services;

import com.roomatefinder.demo.dao.UserDAO;
import com.roomatefinder.demo.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserDAO userDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void saveUser(UserEntity user) {
        userDAO.save(user);
    }

    public UserEntity updateUser(UserEntity user) {
        return userDAO.save(user);
    }

    public Optional<UserEntity> findById(String id) {
        return userDAO.findById(id);
    }

    public UserEntity getUserById(String id) {
        return userDAO.findById(id).orElse(null);
    }

    public UserEntity getUserByEmail(String email) {
        return userDAO.findByEmail(email);
    }

    public UserEntity getUserByUsername(String username) {
        return userDAO.findByUsername(username);
    }

    public List<UserEntity> getAllUsers() {
        return userDAO.findAll();
    }

    public boolean existsByEmail(String email) {
        return userDAO.existsByEmail(email);
    }

    public boolean existsByUsername(String username) {
        return userDAO.existsByUsername(username);
    }

    public void deleteUser(String id) {
        userDAO.deleteById(id);
    }

    public List<UserEntity> getUsersByRole(String role) {
        return userDAO.findByRole(role);
    }

    public List<UserEntity> findActiveUsers() {
        return userDAO.findByIsActiveTrue();
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}