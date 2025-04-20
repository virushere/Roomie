package com.roomatefinder.demo.services;

import com.roomatefinder.demo.dao.UserPreferencesDAO;
import com.roomatefinder.demo.models.UserPreferences;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserPreferencesService {

    private final UserPreferencesDAO userPreferencesDAO;

    @Autowired
    public UserPreferencesService(UserPreferencesDAO userPreferencesDAO) {
        this.userPreferencesDAO = userPreferencesDAO;
    }

    public UserPreferences createPreferences(UserPreferences preferences) {
        System.out.println("Creating preferences for user: " + preferences.getUserId());
        // Initialize collections if they're null
        if (preferences.getPreferredAreas() == null) {
            preferences.setPreferredAreas(new HashSet<>());
        }
        if (preferences.getLanguagesKnown() == null) {
            preferences.setLanguagesKnown(new HashSet<>());
        }
        UserPreferences saved = userPreferencesDAO.save(preferences);
        System.out.println("Created preference with ID: " + saved.getId());
        return saved;
    }

    @Transactional(readOnly = true)
    public UserPreferences getPreferenceByIdWithCollections(Long id) {
        UserPreferences preference = getPreferenceById(id);
        if (preference != null) {
            // Force initialization of collections to avoid lazy loading issues
            if (preference.getPreferredAreas() != null) {
                preference.getPreferredAreas().size();
            }
            if (preference.getLanguagesKnown() != null) {
                preference.getLanguagesKnown().size();
            }
        }
        return preference;
    }

    public UserPreferences getPreferenceById(Long id) {
        return userPreferencesDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Preferences not found with ID: " + id));
    }

    public List<UserPreferences> getPreferencesByUserId(String userId) {
        System.out.println("Service: Getting preferences for user ID: " + userId);
        List<UserPreferences> preferences = userPreferencesDAO.findByUserId(userId);
        System.out.println("Service: Found " + preferences.size() + " preferences");
        return preferences;
    }

    public UserPreferences updatePreferences(Long id, UserPreferences updatedPrefs, String userId) {
        System.out.println("Updating preference ID: " + id + " for user: " + userId);

        // Find the existing preference or throw an exception if not found
        UserPreferences existing = userPreferencesDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Preference not found with ID: " + id));

        System.out.println("Existing preference user ID: " + existing.getUserId());
        // Make sure to assign the current user’s id/email
        existing.setUserId(userId);

        // Update fields from the original version
        existing.setGender(updatedPrefs.getGender());
        existing.setFoodPref(updatedPrefs.getFoodPref());
        existing.setAlcoholPref(updatedPrefs.getAlcoholPref());
        existing.setSmoking(updatedPrefs.getSmoking());
        existing.setCleanlinessRating(updatedPrefs.getCleanlinessRating());
        existing.setSleepSchedule(updatedPrefs.getSleepSchedule());
        existing.setYearOfIntake(updatedPrefs.getYearOfIntake());
        existing.setLeaseOption(updatedPrefs.getLeaseOption());
        existing.setMaxRoommates(updatedPrefs.getMaxRoommates());

        // *** Update the new fields ***
        existing.setDateOfBirth(updatedPrefs.getDateOfBirth());
        existing.setOccupation(updatedPrefs.getOccupation());
        existing.setPartyFriendly(updatedPrefs.getPartyFriendly());
        existing.setStudyWorkSchedule(updatedPrefs.getStudyWorkSchedule());
        existing.setMaxRentPreference(updatedPrefs.getMaxRentPreference());
        existing.setIsPetFriendly(updatedPrefs.getIsPetFriendly());
        existing.setInterests(updatedPrefs.getInterests());
        existing.setPreferredRoommateGender(updatedPrefs.getPreferredRoommateGender());
        existing.setPreferredMinAge(updatedPrefs.getPreferredMinAge());
        existing.setPreferredMaxAge(updatedPrefs.getPreferredMaxAge());
        existing.setGuestFrequency(updatedPrefs.getGuestFrequency());
        existing.setNoiseTolerance(updatedPrefs.getNoiseTolerance());
        existing.setRoommateLifestyle(updatedPrefs.getRoommateLifestyle());
        existing.setRoomTypePreference(updatedPrefs.getRoomTypePreference());

        // Update collections – preferred areas and languages. A null check is done here;
        // adjust as necessary if you expect an empty set or want to keep old values if none provided.
        if (updatedPrefs.getPreferredAreas() != null) {
            existing.setPreferredAreas(updatedPrefs.getPreferredAreas());
        }
        if (updatedPrefs.getLanguagesKnown() != null) {
            existing.setLanguagesKnown(updatedPrefs.getLanguagesKnown());
        }

        UserPreferences saved = userPreferencesDAO.save(existing);
        System.out.println("Updated preference with ID: " + saved.getId());
        return saved;
    }

    public void deletePreferences(Long id) {
        userPreferencesDAO.deleteById(id);
    }

    public Optional<UserPreferences> getPreferencesById(Long id) {
        return userPreferencesDAO.findById(id);
    }

    public Optional<UserPreferences> getPreferencesByIdAndUserId(Long id, String userId) {
        return userPreferencesDAO.findByIdAndUserId(id, userId);
    }

    public List<UserPreferences> getUserPreferences(String userId) {
        return userPreferencesDAO.findByUserId(userId);
    }

    public List<UserPreferences> getAllPreferences() {
        return userPreferencesDAO.findAll();
    }
}
