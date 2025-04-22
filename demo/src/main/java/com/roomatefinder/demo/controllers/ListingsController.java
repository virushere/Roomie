package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.models.UserEntity;
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

//    @PostMapping("/create-preference")
//    public String createPreference(@ModelAttribute UserPreferences preference,
//                                   @AuthenticationPrincipal UserDetails userDetails) {
//        log.debug("Creating preference with initial values: {}", preference);
////        preference.setUserId(userDetails.getUsername());
////        UserPreferences created = userPreferencesService.createPreferences(preference);
//        UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());
//        preference.setUserId(currentUser.getId());
//        UserPreferences created = userPreferencesService.createPreferences(preference);
//        userPreferencesService.createPreferences(preference);
//        log.debug("Created preference: {}", created);
//        return "redirect:/users/my-listings?success=preference_created";
//    }
@PostMapping("/create-preference")
public String createPreference(@ModelAttribute UserPreferences preference,
                               @RequestParam(value = "age", required = false) Integer ageInt,
                               @AuthenticationPrincipal UserDetails userDetails) {
    log.debug("Creating preference with initial values: {}", preference);

    // Get current user
    UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());
    preference.setUserId(currentUser.getId());

    // Create the preferences
    UserPreferences created = userPreferencesService.createPreferences(preference);
    log.debug("Created preference: {}", created);

    // Update user's age if provided
    if (ageInt != null) {
        Byte age = ageInt.byteValue();
        currentUser.setAge(age);
        userService.updateUser(currentUser);
        log.debug("Updated user (email={}) age to {}", currentUser.getEmail(), age);
    }

    return "redirect:/users/my-listings?success=preference_created";
}

    @GetMapping("/edit-preference/{id}")
    public String showEditForm(
            @PathVariable Long id,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails) {

        // 1) Lookup the current UserEntity (so we can get its UUID)
        UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());
        String currentUserId = currentUser.getId();

        // 2) Fetch the preference (with collections initialized)
        UserPreferences preference = userPreferencesService.getPreferenceByIdWithCollections(id);
        if (preference == null) {
            log.warn("No preference found with ID: {}", id);
            return "redirect:/users/my-listings?error=preference_not_found";
        }

        // 3) Enforce ownership by comparing UUIDs (not emails)
        if (!currentUserId.equals(preference.getUserId())) {
            log.warn("User {} (id={}) not authorized to edit preference with ID {}",
                    userDetails.getUsername(), currentUserId, id);
            return "redirect:/users/my-listings?error=not_authorized";
        }

        // 4) Populate the model with the preference and all enum options
        model.addAttribute("preference", preference);
        model.addAttribute("genderOptions", UserPreferences.Gender.values());
        model.addAttribute("foodPreferences", UserPreferences.FoodPreference.values());
        model.addAttribute("alcoholPreferences", UserPreferences.AlcoholPreference.values());
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

        // for date‐of‐birth max‐date and yearOfIntake validation
        model.addAttribute("currentDate", LocalDate.now());
        model.addAttribute("currentYear", LocalDate.now().getYear());

        return "edit-preference";
    }


    @PostMapping("/update-preference/{id}")
    public String updatePreference(
            @PathVariable Long id,
            @RequestParam("age") Integer ageInt,
            @ModelAttribute UserPreferences updatedPrefs,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.debug(">> Enter updatePreference(id={}, updatedPrefs={})", id, updatedPrefs);

        // 1) Identify current user
        String email = userDetails.getUsername();
        log.debug("Authenticated principal email = {}", email);
        UserEntity currentUser = userService.getUserByEmail(email);
        if (currentUser == null) {
            log.error("No UserEntity found for email {}", email);
            return "redirect:/users/my-listings?error=user_not_found";
        }
        String currentUserId = currentUser.getId();
        log.debug("Mapped to UserEntity id = {}", currentUserId);

        // 2) Bind FK on preferences
        updatedPrefs.setUserId(currentUserId);
        log.debug("Set updatedPrefs.userId = {}", currentUserId);

        // 3) Validate date of birth
        LocalDate dob = updatedPrefs.getDateOfBirth();
        log.debug("Incoming dateOfBirth = {}", dob);
        if (dob != null && dob.isAfter(LocalDate.now())) {
            log.error("Invalid DOB: {} is in the future", dob);
            throw new IllegalArgumentException("Date of Birth cannot be in the future");
        }

        try {
            // 4) Load existing prefs
            log.debug("Loading existing preferences with id = {}", id);
            UserPreferences existing = userPreferencesService.getPreferenceById(id);

            // 5) Decide create vs update
            if (existing == null) {
                log.warn("No existing preferences found for id = {} → creating new", id);
                userPreferencesService.createPreferences(updatedPrefs);
                log.debug("Created new preferences = {}", updatedPrefs);
                return "redirect:/users/my-listings?success=preference_created";
            }

            // 6) Ownership check
            if (!currentUserId.equals(existing.getUserId())) {
                log.warn("Ownership mismatch: prefs.userId = {} but currentUserId = {} → creating new",
                        existing.getUserId(), currentUserId);
                userPreferencesService.createPreferences(updatedPrefs);
                log.debug("Created new preferences = {}", updatedPrefs);
                return "redirect:/users/my-listings?success=preference_created";
            }

            log.debug("Existing preferences belongs to current user → updating");
            log.debug("Before update: {}", existing);
            log.debug("Updated fields preview: Gender {}→{}, FoodPref {}→{}, #langs {}→{}",
                    existing.getGender(), updatedPrefs.getGender(),
                    existing.getFoodPref(), updatedPrefs.getFoodPref(),
                    existing.getLanguagesKnown()  != null ? existing.getLanguagesKnown().size() : 0,
                    updatedPrefs.getLanguagesKnown() != null ? updatedPrefs.getLanguagesKnown().size() : 0
            );

            // 7) Persist update
            userPreferencesService.updatePreferences(id, updatedPrefs, currentUserId);
            log.debug("Preference successfully updated: {}", updatedPrefs);

            Byte age = (ageInt != null) ? ageInt.byteValue() : null;
            currentUser.setAge(age);
            userService.updateUser(currentUser);
            log.debug("Updated user (email={}) age to {}", email, age);
            return "redirect:/users/my-listings?success=preference_updated";

        } catch (RuntimeException e) {
            log.error("Exception in updatePreference: {}", e.getMessage(), e);
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