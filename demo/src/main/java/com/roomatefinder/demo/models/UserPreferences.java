package com.roomatefinder.demo.models;

import com.roomatefinder.demo.Helper.UppercaseEnumConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.EnumType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Convert;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.FetchType;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Set;

@Data
@Entity
@Table(name = "user_preferences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPreferences {


    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum FoodPreference {
        VEGAN, VEGETARIAN, NON_VEGETARIAN, JAIN
    }

    public enum AlcoholPreference {
        YES, NO, OCCASIONALLY
    }

    public enum PreferredArea {
        JAMAICA_PLAIN, FENWAY, KENMORE, HUNTINGTON_AVENUE
    }

    public enum LeaseOption {
        SHORT_TERM, LONG_TERM
    }

    public enum Languages {
        ENGLISH, SPANISH, FRENCH, GERMAN, CHINESE, HINDI, MARATHI, GUJARATI, TAMIL
    }

    public enum Occupation {
        STUDENT, WORKING_PROFESSIONAL, OTHER
    }

    public enum PartyFriendly {
        YES, NO, SOMETIMES
    }

    public enum StudyWorkSchedule {
        DAY, NIGHT, FLEXIBLE
    }

    public enum PreferredRoommateGender {
        MALE, FEMALE, ANY
    }

    public enum GuestFrequency {
        RARELY, SOMETIMES, OFTEN
    }

    public enum NoiseTolerance {
        LOW, MEDIUM, HIGH
    }

    public enum RoommateLifestyle {
        INTROVERT, EXTROVERT, BALANCED
    }

    public enum RoomTypePreference {
        PRIVATE, SHARED, NO_PREFERENCE
    }

    // Primary key
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the user (storing user id as a string)
    @Column(name = "user_id")
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "enum('MALE','FEMALE','OTHER')")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "food_pref", columnDefinition = "enum('VEGAN','VEGETARIAN','NON_VEGETARIAN','JAIN')")
    private FoodPreference foodPref;

    @Enumerated(EnumType.STRING)
    @Column(name = "alcohol_pref", columnDefinition = "enum('YES','NO','OCCASIONALLY')")
    private AlcoholPreference alcoholPref;

    @Transient  // This field doesn't persist in this table but gets transferred to the user entity
    private Byte age;

    // Smoking: using Boolean so that null is allowed
    private Boolean smoking;

    @Column(name = "cleanliness_rating")
    private Integer cleanlinessRating;

    @Column(name = "sleep_schedule")
    private String sleepSchedule;

    @Column(name = "year_of_intake")
    private Integer yearOfIntake;

    @Enumerated(EnumType.STRING)
    @Column(name = "lease_option", columnDefinition = "enum('SHORT_TERM','LONG_TERM')")
    private LeaseOption leaseOption;

    @Column(name = "max_roommates")
    private Integer maxRoommates;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @PastOrPresent(message = "Date of Birth must not be in the future")
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "occupation", columnDefinition = "enum('STUDENT','WORKING_PROFESSIONAL','OTHER')")
    private Occupation occupation;

    @Enumerated(EnumType.STRING)
    @Column(name = "party_friendly", columnDefinition = "enum('YES','NO','SOMETIMES')")
    private PartyFriendly partyFriendly;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_work_schedule", columnDefinition = "enum('DAY','NIGHT','FLEXIBLE')")
    private StudyWorkSchedule studyWorkSchedule;

    @Column(name = "max_rent_preference")
    private Integer maxRentPreference;

    @Column(name = "is_pet_friendly")
    private Boolean isPetFriendly;

    @Column(name = "interests", length = 500)
    private String interests;

    @Enumerated(EnumType.STRING)
    @Column(name = "preferred_roommate_gender", columnDefinition = "enum('MALE','FEMALE','ANY')")
    private PreferredRoommateGender preferredRoommateGender;

    @Column(name = "preferred_min_age")
    private Integer preferredMinAge;

    @Column(name = "preferred_max_age")
    private Integer preferredMaxAge;

    @Enumerated(EnumType.STRING)
    @Column(name = "guest_frequency", columnDefinition = "enum('RARELY','SOMETIMES','OFTEN')")
    private GuestFrequency guestFrequency;

    @Enumerated(EnumType.STRING)
    @Column(name = "noise_tolerance", columnDefinition = "enum('LOW','MEDIUM','HIGH')")
    private NoiseTolerance noiseTolerance;

    @Enumerated(EnumType.STRING)
    @Column(name = "roommate_lifestyle", columnDefinition = "enum('INTROVERT','EXTROVERT','BALANCED')")
    private RoommateLifestyle roommateLifestyle;

    @Enumerated(EnumType.STRING)
    @Column(name = "room_type_preference", columnDefinition = "enum('PRIVATE','SHARED','NO_PREFERENCE')")
    private RoomTypePreference roomTypePreference;

    // Collections for multivalued fields
    // Set fetch to EAGER to avoid lazy initialization issues during logging/updating.
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "preferred_areas", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "preferred_area")
    @Enumerated(EnumType.STRING)
    private Set<PreferredArea> preferredAreas;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "languages_known", joinColumns = @JoinColumn(name = "preference_id"))
    @Column(name = "language")
    @Convert(converter = UppercaseEnumConverter.class)
    private Set<Languages> languagesKnown;

    @Override
    public String toString() {
        return "UserPreferences(id=" + id + ", userId=" + userId + ")";
    }
}
