package com.roomatefinder.demo.models;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Base64;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "profile_picture")
    private String profilePicture;

    @Lob
    @Column(name = "profile_image_data", columnDefinition = "LONGBLOB")
    private byte[] profileImageData;

    @Column(name = "profile_image_type")
    private String profileImageType;

    @Column(name = "bio")
    private String bio;

    @Column(name = "university")
    private String university;

    @Column(name = "preferences")
    private String preferences;

    @Column(name = "user_type")
    private String userType;

    @Column(name = "is_active")
    private Boolean isActive = true; // Default value

    @Column(name = "has_room")
    private Boolean has_room = false; // Default value

    @Column(name = "has_preferences")
    private Boolean has_preferences = false; // Default value

    @CreationTimestamp
    @Column(name = "created_at", updatable = false) // updatable=false prevents updates
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Check if user has a profile image
    public boolean hasProfileImage() {
        return profileImageData != null && profileImageData.length > 0;
    }

    public String getBase64Image() {
        if (profileImageData != null) {
            return Base64.getEncoder().encodeToString(profileImageData);
        }
        return null;
    }
}