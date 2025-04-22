package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.DTO.UserProfileFormDTO;
import com.roomatefinder.demo.models.RoomInfo;
import com.roomatefinder.demo.models.UserEntity;
import com.roomatefinder.demo.models.UserPreferences;
import com.roomatefinder.demo.services.FileStorageService;
import com.roomatefinder.demo.services.UserPreferencesService;
import com.roomatefinder.demo.services.UserService;
import com.roomatefinder.demo.services.RoomInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final FileStorageService fileStorageService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoomInfoService roomInfoService;

    @Autowired
    private UserPreferencesService userPreferencesService;

    // View for user profile page
    @GetMapping("/profile")
    public String showUserProfile(Principal principal, Model model) {
        if (principal != null) {
            UserEntity user = userService.getUserByEmail(principal.getName());
            model.addAttribute("user", user);
            return "user-profile";
        }
        return "redirect:/auth/login";
    }


    @PostMapping("/profile")
    public String updateUserProfile(
            @ModelAttribute("user") UserProfileFormDTO formDTO,
            @RequestParam(value = "profileImageFile", required = false) MultipartFile profileImageFile,
            Principal principal) {

        // Debug
        System.out.println("Form DTO received: " + formDTO.getFirstName() + ", " + formDTO.getLastName() + ", " + formDTO.getEmail());
        System.out.println("Is Active from form: " + formDTO.getIsActive());

        // Get the current user
        String email = principal.getName();
        UserEntity existingUser = userService.getUserByEmail(email);

        if (existingUser == null) {
            return "redirect:/auth/login";
        }

        // Important: Only update fields if they are not null
        if (formDTO.getFirstName() != null) existingUser.setFirstName(formDTO.getFirstName());
        if (formDTO.getLastName() != null) existingUser.setLastName(formDTO.getLastName());
        // Don't update email from form as it's sensitive
        if (formDTO.getPhoneNumber() != null) existingUser.setPhoneNumber(formDTO.getPhoneNumber());
        if (formDTO.getBio() != null) existingUser.setBio(formDTO.getBio());
        //existingUser.setIsActive(formDTO.getIsActive());
        // Explicitly handle the Boolean conversion to avoid null issues
        // Debug
        System.out.println("Form DTO received: " + formDTO.getFirstName() + ", " + formDTO.getLastName() + ", " + formDTO.getEmail());
        System.out.println("Is Active from form (raw): " + formDTO.getIsActive());
        System.out.println("Is Active from form (class): " + (formDTO.getIsActive() != null ? formDTO.getIsActive().getClass().getName() : "null"));
        System.out.println("Existing user active status: " + existingUser.getIsActive());

        // Explicit handling
        if (formDTO.getIsActive() != null) {
            existingUser.setIsActive(formDTO.getIsActive());
            System.out.println("Setting isActive to: " + formDTO.getIsActive());
        } else {
            existingUser.setIsActive(false);
            System.out.println("IsActive was null, defaulting to false");
        }

        // After setting
        System.out.println("User isActive after setting: " + existingUser.getIsActive());
        System.out.println("Setting isActive to: " + formDTO.getIsActive());
        if (formDTO.getUniversity() != null) existingUser.setUniversity(formDTO.getUniversity());

        // Handle file upload if a file was provided
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            try {
                // Store the original filename
                String originalFilename = profileImageFile.getOriginalFilename();
                // Generate a unique filename to prevent collisions
                String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;

                // Store both the image data and the filename
                existingUser.setProfileImageData(profileImageFile.getBytes());
                existingUser.setProfileImageType(profileImageFile.getContentType());
                existingUser.setProfilePicture(uniqueFilename); // Store the unique filename

                System.out.println("Image stored for user: " + existingUser.getId());
                System.out.println("Image type: " + existingUser.getProfileImageType());
                System.out.println("Image filename: " + existingUser.getProfilePicture());
                System.out.println("Image size: " + existingUser.getProfileImageData().length + " bytes");

            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/users/profile?error=true";
            }
        }

        // Save the updated user
        userService.updateUser(existingUser);

        return "redirect:/users/profile?success=true";
    }

    // REST API endpoints below
    // Update user (REST API)
    @PutMapping("/update/{id}")
    @ResponseBody
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody UserEntity updatedUser) {
        Optional<UserEntity> existingUser = Optional.ofNullable(userService.getUserById(id));

        if (existingUser.isEmpty()) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // If password is being updated, encode it
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        } else {
            // Preserve existing password
            updatedUser.setPassword(existingUser.get().getPassword());
        }

        // Preserve created date and active status
        updatedUser.setCreatedAt(existingUser.get().getCreatedAt());
        updatedUser.setIsActive(existingUser.get().getIsActive());
        updatedUser.setUpdatedAt(LocalDateTime.now());
        updatedUser.setId(id);

        UserEntity savedUser = userService.updateUser(updatedUser);
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    // Get user by ID (REST API)
    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        UserEntity user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Get current user (REST API)
    @GetMapping("/current-user")
    @ResponseBody
    public ResponseEntity<?> getCurrentUser(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Principal is null");
        }

        String email = principal.getName();
        UserEntity user = userService.getUserByEmail(email);

        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        return ResponseEntity.ok(user);
    }

    @GetMapping("/my-listings")
    public String showMyListings(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        // still useful to know who's logged in
        String email = principal.getName();
        log.debug("Current user email: {}", email);

        // fetch the UserEntity by email
        UserEntity currentUser = userService.getUserByEmail(email);
        if (currentUser == null) {
            return "redirect:/auth/login";
        }

        // show the user's own profile as a "roommate listing" if they're active
        if (Boolean.TRUE.equals(currentUser.getIsActive())) {
            model.addAttribute("roommateListings", List.of(currentUser));
        } else {
            model.addAttribute("roommateListings", Collections.emptyList());
        }

        // pull in any rooms they've created
        List<RoomInfo> roomListings = roomInfoService.getRoomsByUserId(currentUser.getId());
        model.addAttribute("roomListings", roomListings);

        // **now** lookup their preferences by userId instead of email
        String userId = currentUser.getId();
        List<UserPreferences> preferences = userPreferencesService.getPreferencesByUserId(userId);
        log.debug("Found {} preferences for userId {}", preferences.size(), userId);
        model.addAttribute("userPreferences", preferences);

        return "my-listings";
    }

    // Add this method to your UserController
    @GetMapping("/profile-image/{userId}")
    public ResponseEntity<byte[]> getProfileImage(@PathVariable String userId) {
        UserEntity user = userService.getUserById(userId);
        if (user != null && user.getProfileImageData() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(user.getProfileImageType()));
            return new ResponseEntity<>(user.getProfileImageData(), headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
