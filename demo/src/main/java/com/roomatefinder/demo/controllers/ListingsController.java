//package com.roomatefinder.demo.controllers;
//
//import com.roomatefinder.demo.models.UserPreferences;
//import com.roomatefinder.demo.services.RoomInfoService;
//import com.roomatefinder.demo.services.UserPreferencesService;
//import com.roomatefinder.demo.services.UserService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import java.time.LocalDate;
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//@RequestMapping("/users")
//@Slf4j
//public class ListingsController {
//
//    @Autowired
//    private RoomInfoService roomInfoService;
//
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private UserPreferencesService userPreferencesService;
//
//    @GetMapping("/create-preference")
//    public String showCreatePreferenceForm(Model model) {
//        model.addAttribute("preference", new UserPreferences());
//        model.addAttribute("foodPreferences", UserPreferences.FoodPreference.values());
//        model.addAttribute("alcoholPreferences", UserPreferences.AlcoholPreference.values());
//        model.addAttribute("leaseOptions", UserPreferences.LeaseOption.values());
//        model.addAttribute("areaOptions", UserPreferences.PreferredArea.values());
//        return "create-preference";
//    }
//
//    @PostMapping("/create-preference")
//    public String createPreference(@ModelAttribute UserPreferences preference,
//                                   @AuthenticationPrincipal UserDetails userDetails) {
//        preference.setUserId(userDetails.getUsername());
//        userPreferencesService.createPreferences(preference);
//        return "redirect:/users/my-listings?success=preference_created";
//    }
//    @GetMapping("/edit-preference/{id}")
//    public String showEditForm(@PathVariable String id, Model model,
//                               @AuthenticationPrincipal UserDetails userDetails) {
//        try {
//            Long preferenceId = Long.parseLong(id);
//
//            // Use a service method that explicitly fetches the collection
//            UserPreferences preference = userPreferencesService.getPreferenceByIdWithCollections(preferenceId);
//
//            // Fallback if no such method exists
//            if (preference == null) {
//                return "redirect:/users/my-listings?error=preference_not_found";
//            }
//
//            // Verify the preference belongs to the current user
//            if (!preference.getUserId().equals(userDetails.getUsername())) {
//                return "redirect:/users/my-listings?error=not_authorized";
//            }
//
//            model.addAttribute("preference", preference);
//
//            // Add enum values for dropdowns
//            model.addAttribute("genders", UserPreferences.Gender.values());
//            model.addAttribute("foodPreferences", UserPreferences.FoodPreference.values());
//            model.addAttribute("alcoholPreferences", UserPreferences.AlcoholPreference.values());
//            model.addAttribute("leaseOptions", UserPreferences.LeaseOption.values());
//
//            // Add current year for yearOfIntake validation
//            model.addAttribute("currentYear", LocalDate.now().getYear());
//
//            // Convert preferredAreas enum values to strings for checkboxes
//            List<String> preferredAreas = Arrays.stream(UserPreferences.PreferredArea.values())
//                    .map(Enum::name)
//                    .collect(Collectors.toList());
//            model.addAttribute("preferredAreas", preferredAreas);
//
//            // Convert language enums to strings for checkboxes
//            List<String> languages = Arrays.stream(UserPreferences.Languages.values())
//                    .map(Enum::name)
//                    .collect(Collectors.toList());
//            model.addAttribute("languages", languages);
//
//            return "edit-preference";
//        } catch (Exception e) {
//            log.error("Error loading preference: " + e.getMessage(), e);
//            return "redirect:/users/my-listings?error=load_failed";
//        }
//    }
//    @PostMapping("/update-preference/{id}")
//    public String updatePreference(@PathVariable Long id,
//                                   @ModelAttribute UserPreferences updatedPrefs,
//                                   @AuthenticationPrincipal UserDetails userDetails) {
//        String email = userDetails.getUsername();
//        updatedPrefs.setUserId(email);
//
//        try {
//            // First check if the preference exists and belongs to this user
//            UserPreferences existing = null;
//            try {
//                existing = userPreferencesService.getPreferenceById(id);
//                // Check if it belongs to the current user
//                if (!email.equals(existing.getUserId())) {
//                    throw new RuntimeException("Preference doesn't belong to current user");
//                }
//            } catch (RuntimeException e) {
//                // Preference not found or doesn't belong to user
//                log.info("Creating new preference instead of updating");
//                userPreferencesService.createPreferences(updatedPrefs);
//                return "redirect:/users/my-listings?success=preference_created";
//            }
//
//            // Otherwise, update the existing preference
//            userPreferencesService.updatePreferences(id, updatedPrefs, email);
//            return "redirect:/users/my-listings?success=preference_updated";
//        } catch (Exception e) {
//            log.error("Error updating preference: " + e.getMessage(), e);
//            return "redirect:/users/my-listings?error=update_failed";
//        }
//    }
//
//    @PostMapping("/delete-preference/{id}")
//    public String deletePreference(@PathVariable String id) {
//        Long preferenceId = Long.parseLong(id); // Convert to Long
//        userPreferencesService.deletePreferences(preferenceId);
//        return "redirect:/users/my-listings?success=preference_deleted";
//    }
//}
package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.models.UserPreferences;
import com.roomatefinder.demo.services.RoomInfoService;
import com.roomatefinder.demo.services.UserPreferencesService;
import com.roomatefinder.demo.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/users")
@Slf4j
public class ListingsController {

    @Autowired
    private RoomInfoService roomInfoService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPreferencesService userPreferencesService;

    @GetMapping("/create-preference")
    public String showCreatePreferenceForm(Model model) {
        model.addAttribute("preference", new UserPreferences());
        // Existing enum options
        model.addAttribute("genderOptions", UserPreferences.Gender.values());
        model.addAttribute("foodPreferences", UserPreferences.FoodPreference.values());
        model.addAttribute("alcoholPreferences", UserPreferences.AlcoholPreference.values());
        model.addAttribute("leaseOptions", UserPreferences.LeaseOption.values());
        model.addAttribute("preferredAreas", UserPreferences.PreferredArea.values());
        model.addAttribute("languages", UserPreferences.Languages.values());
        // New enum options added for extended filtering and profile information
        model.addAttribute("occupations", UserPreferences.Occupation.values());
        model.addAttribute("partyFriendlyOptions", UserPreferences.PartyFriendly.values());
        model.addAttribute("studyWorkScheduleOptions", UserPreferences.StudyWorkSchedule.values());
        model.addAttribute("preferredRoommateGenderOptions", UserPreferences.PreferredRoommateGender.values());
        model.addAttribute("guestFrequencyOptions", UserPreferences.GuestFrequency.values());
        model.addAttribute("noiseToleranceOptions", UserPreferences.NoiseTolerance.values());
        model.addAttribute("roommateLifestyleOptions", UserPreferences.RoommateLifestyle.values());
        model.addAttribute("roomTypePreferenceOptions", UserPreferences.RoomTypePreference.values());
        return "create-preference";
    }

    @PostMapping("/create-preference")
    public String createPreference(@ModelAttribute UserPreferences preference,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        log.debug("Creating preference with initial values: {}", preference);
        preference.setUserId(userDetails.getUsername());
        UserPreferences created = userPreferencesService.createPreferences(preference);
        log.debug("Created preference: {}", created);
        return "redirect:/users/my-listings?success=preference_created";
    }

    @GetMapping("/edit-preference/{id}")
    public String showEditForm(@PathVariable String id, Model model,
                               @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long preferenceId = Long.parseLong(id);
            UserPreferences preference = userPreferencesService.getPreferenceByIdWithCollections(preferenceId);
            if (preference == null) {
                log.warn("No preference found with ID: {}", preferenceId);
                return "redirect:/users/my-listings?error=preference_not_found";
            }
            if (!preference.getUserId().equals(userDetails.getUsername())) {
                log.warn("User {} not authorized to edit preference with ID: {}", userDetails.getUsername(), preferenceId);
                return "redirect:/users/my-listings?error=not_authorized";
            }
            log.debug("Loaded preference for editing: {}", preference);
            model.addAttribute("preference", preference);
            model.addAttribute("genderOptions", UserPreferences.Gender.values());
            model.addAttribute("foodPreferences", UserPreferences.FoodPreference.values());
            model.addAttribute("alcoholPreferences", UserPreferences.AlcoholPreference.values());
            model.addAttribute("currentDate", java.time.LocalDate.now());
            model.addAttribute("occupations", UserPreferences.Occupation.values());
            model.addAttribute("partyFriendlyOptions", UserPreferences.PartyFriendly.values());
            model.addAttribute("studyWorkScheduleOptions", UserPreferences.StudyWorkSchedule.values());
            model.addAttribute("leaseOptions", UserPreferences.LeaseOption.values());
            model.addAttribute("preferredAreas", UserPreferences.PreferredArea.values());
            model.addAttribute("preferredRoommateGenderOptions", UserPreferences.PreferredRoommateGender.values());
            model.addAttribute("guestFrequencyOptions", UserPreferences.GuestFrequency.values());
            model.addAttribute("noiseToleranceOptions", UserPreferences.NoiseTolerance.values());
            model.addAttribute("roommateLifestyleOptions", UserPreferences.RoommateLifestyle.values());
            model.addAttribute("roomTypePreferenceOptions", UserPreferences.RoomTypePreference.values());
            model.addAttribute("languages", UserPreferences.Languages.values());
            model.addAttribute("currentYear", LocalDate.now().getYear());
            return "edit-preference";
        } catch (Exception e) {
            log.error("Error loading preference: " + e.getMessage(), e);
            return "redirect:/users/my-listings?error=load_failed";
        }
    }

    @PostMapping("/update-preference/{id}")
    public String updatePreference(@PathVariable Long id,
                                   @ModelAttribute UserPreferences updatedPrefs,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        updatedPrefs.setUserId(email);
        if (updatedPrefs.getDateOfBirth() != null && updatedPrefs.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Date of Birth cannot be a future date");
        }
        log.debug("Attempting to update preference ID: {} for user: {}", id, email);
        if (updatedPrefs.getLanguagesKnown() != null) {
            updatedPrefs.getLanguagesKnown().size(); // Force initialization
        }
        try {
            UserPreferences existing = userPreferencesService.getPreferenceById(id);
            if (existing == null || !email.equals(existing.getUserId())) {
                log.warn("No existing preference found for ID: {} or it doesn't belong to user: {}", id, email);
                log.info("Creating new preference instead of updating");
                userPreferencesService.createPreferences(updatedPrefs);
                return "redirect:/users/my-listings?success=preference_created";
            }
            log.debug("Existing preference before update: {}", existing);
            // For debugging, log each updated property (sample logging for a few fields)
            log.debug("Updating fields: Gender: {} -> {}, FoodPref: {} -> {}, LanguagesKnown count: {} -> {}",
                    existing.getGender(), updatedPrefs.getGender(),
                    existing.getFoodPref(), updatedPrefs.getFoodPref(),
                    (existing.getLanguagesKnown() != null ? existing.getLanguagesKnown().size() : 0),
                    (updatedPrefs.getLanguagesKnown() != null ? updatedPrefs.getLanguagesKnown().size() : 0));

            if (updatedPrefs.getLanguagesKnown() != null) {
                updatedPrefs.getLanguagesKnown().size(); // Force initialization
            }
            if (existing.getLanguagesKnown() != null) {
                existing.getLanguagesKnown().size(); // Force initialization
            }

            userPreferencesService.updatePreferences(id, updatedPrefs, email);
            log.debug("Preference successfully updated: {}", updatedPrefs);
            return "redirect:/users/my-listings?success=preference_updated";
        } catch (Exception e) {
            log.error("Error updating preference: " + e.getMessage(), e);
            return "redirect:/users/my-listings?error=update_failed";
        }
    }

    @PostMapping("/delete-preference/{id}")
    public String deletePreference(@PathVariable String id) {
        try {
            Long preferenceId = Long.parseLong(id);
            log.debug("Deleting preference with ID: {}", preferenceId);
            userPreferencesService.deletePreferences(preferenceId);
            return "redirect:/users/my-listings?success=preference_deleted";
        } catch (Exception e) {
            log.error("Error deleting preference: " + e.getMessage(), e);
            return "redirect:/users/my-listings?error=delete_failed";
        }
    }
}