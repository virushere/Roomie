//package com.roomatefinder.demo.controllers;
//
//import com.roomatefinder.demo.models.RegistrationRequest;
//import com.roomatefinder.demo.models.UserEntity;
//import com.roomatefinder.demo.repositories.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.time.LocalDateTime;
//
//@Controller  // Changed from @RestController to support view rendering
//@RequestMapping("/auth")
//public class RegistrationController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    // GET endpoint to display registration form
//    @GetMapping("/register")
//    public String showRegistrationForm() {
//        return "registration";  // Returns the registration.jsp view
//    }
//
//    // POST endpoint for form submission
//    @PostMapping("/register-form")
//    public String processRegistrationForm(@ModelAttribute RegistrationRequest request, Model model) {
//        // Check if email already exists
//        if (userRepository.findByEmail(request.getEmail()) != null) {
//            model.addAttribute("error", "Email already exists");
//            return "registration";  // Return to registration form with error
//        }
//
//        // Create new user
//        UserEntity user = createUserEntity(request);
//        userRepository.save(user);
//
//        model.addAttribute("success", "Registration successful! Please log in.");
//        return "redirect:/auth/registration-success";  // Redirect to thank you page
//    }
//
//    // Keep original REST API endpoint for programmatic registration
//    @PostMapping("/register")
//    @ResponseBody  // Ensures this method returns JSON
//    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
//        // Check if email already exists
//        if (userRepository.findByEmail(request.getEmail()) != null) {
//            return new ResponseEntity<>("Email already exists", HttpStatus.BAD_REQUEST);
//        }
//
//        // Create new user
//        UserEntity user = createUserEntity(request);
//        userRepository.save(user);
//
//        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
//    }
//
//    @GetMapping("/registration-success")
//    public String showRegistrationSuccess() {
//        return "registration-success";  // Returns the registration-success.jsp view
//    }
//
//    // Helper method to create user entity
//    private UserEntity createUserEntity(RegistrationRequest request) {
//        return UserEntity.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .phoneNumber(request.getPhoneNumber())
//                .bio(request.getBio())
//                .preferences(request.getPreferences())
//                .userType(request.getUserType() != null ? request.getUserType() : "USER")
//                .isActive(true)
//                .createdAt(LocalDateTime.now())
//                .updatedAt(LocalDateTime.now())
//                .build();
//    }
//}
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