package com.roomatefinder.demo.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.Base64;

@Entity
@Table(name = "room_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
public class RoomInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(nullable = false)
    private int rent;

    @Column(name = "rent_negotiable")
    private Boolean rentNegotiable;

    @Column(name = "utilities_cost")
    private Integer utilitiesCost;

    @Column(name = "sublet_available")
    private Boolean subletAvailable;

    @Column(name = "transport_options")
    private String transportOptions;

    @Column(name = "num_beds")
    private Integer numBeds;

    @Column(name = "current_occupancy")
    private Integer currentOccupancy;

    @Column(name = "total_capacity")
    private Integer totalCapacity;

    @Column(name = "available_from")
    private LocalDate availableFrom;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String address;
    private String city;
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Column(name = "image_filename")
    private String imageFilename;

    @Column(name = "image_data", columnDefinition = "LONGBLOB")
    @Lob
    private byte[] imageData;

    @Column(name = "image_type")
    private String imageType;

    // Added fields based on the JSON schema
    @Column(name = "num_baths")
    private Float numBaths;

    @Column(name = "square_feet")
    private Integer squareFeet;

    @Column(name = "is_furnished")
    private Boolean isFurnished;

    @Column(name = "has_parking")
    private Boolean hasParking;

    @Column(name = "has_laundry")
    private Boolean hasLaundry;

    @Column(name = "pets_allowed")
    private Boolean petsAllowed;

    @Column(name = "nearby_universities")
    private String nearbyUniversities;

    @Column(name = "available_until")
    private LocalDate availableUntil;

    @Column(name = "distance_to_university")
    private Integer distanceToUniversity;

    // Helper methods for the view
    public boolean hasImage() {
        return imageData != null && imageData.length > 0 && imageType != null;
    }

    public String getBase64Image() {
        if (hasImage()) {
            return Base64.getEncoder().encodeToString(imageData);
        }
        return "";
    }

    // Explicit getters and setters for the Boolean fields to ensure they are never null
    public Boolean getIsFurnished() {
        return isFurnished != null ? isFurnished : false;
    }

    public void setIsFurnished(Boolean isFurnished) {
        this.isFurnished = isFurnished != null ? isFurnished : false;
    }

    public Boolean getHasParking() {
        return hasParking != null ? hasParking : false;
    }

    public void setHasParking(Boolean hasParking) {
        this.hasParking = hasParking != null ? hasParking : false;
    }

    public Boolean getHasLaundry() {
        return hasLaundry != null ? hasLaundry : false;
    }

    public void setHasLaundry(Boolean hasLaundry) {
        this.hasLaundry = hasLaundry != null ? hasLaundry : false;
    }

    public Boolean getPetsAllowed() {
        return petsAllowed != null ? petsAllowed : false;
    }

    public void setPetsAllowed(Boolean petsAllowed) {
        this.petsAllowed = petsAllowed != null ? petsAllowed : false;
    }
}