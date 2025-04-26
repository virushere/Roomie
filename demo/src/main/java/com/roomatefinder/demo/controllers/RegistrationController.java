package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.models.RegistrationRequest;
import com.roomatefinder.demo.models.UserEntity;
import com.roomatefinder.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/auth")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping("/register-form")
    public String processRegistrationForm(@ModelAttribute RegistrationRequest request, Model model) {
        // Check if email already exists
        if (userService.existsByEmail(request.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "registration";
        }

        // Create new user
        UserEntity user = createUserEntity(request);
        userService.saveUser(user);

        model.addAttribute("success", "Registration successful! Please log in.");
        return "redirect:/auth/registration-success";
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        // Check if email already exists
        if (userService.existsByEmail(request.getEmail())) {
            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
        }

        // Create new user
        UserEntity user = createUserEntity(request);
        userService.saveUser(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }

    @GetMapping("/registration-success")
    public String showRegistrationSuccess() {
        return "registration-success";
    }

    private UserEntity createUserEntity(RegistrationRequest request) {
        return UserEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .bio(request.getBio())
                .preferences(request.getPreferences())
                .userType(request.getUserType() != null ? request.getUserType() : "USER")
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}