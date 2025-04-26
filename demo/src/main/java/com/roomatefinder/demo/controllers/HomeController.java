package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.config.PaginationConfig;
import com.roomatefinder.demo.models.UserEntity;
import com.roomatefinder.demo.models.RoomInfo;
import com.roomatefinder.demo.models.UserPreferences;
import com.roomatefinder.demo.services.UserPreferencesService;
import com.roomatefinder.demo.services.UserService;
import com.roomatefinder.demo.services.RoomInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.security.Principal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RoomInfoService roomInfoService;

    @Autowired
    private UserPreferencesService userPreferencesService;

    @Autowired
    private PaginationConfig paginationConfig;

    // Dashboard view
    @GetMapping("/dashboard")
    public String showDashboard(Principal principal, Model model) {
        if (principal != null) {
            UserEntity user = userService.getUserByEmail(principal.getName());
            model.addAttribute("user", user);
        }
        return "dashboard";
    }

    // REST API endpoints
    @GetMapping("/users")
    @ResponseBody
    public List<UserEntity> getUsers() {
        logger.debug("Getting all users");
        return userService.getAllUsers();
    }

    @GetMapping("/current-user")
    @ResponseBody
    public String getLoggedInUser(Principal principal) {
        return principal.getName();
    }

    @GetMapping("/roommate-listings")
    public String showRoommateListings(
            Principal principal,

            // core user filters
            @RequestParam(required = false) String university,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String foodPref,
            @RequestParam(required = false) String alcoholPref,
            @RequestParam(required = false) Boolean smoking,
            @RequestParam(required = false) Integer cleanlinessRating,
            @RequestParam(required = false) String sleepSchedule,
            @RequestParam(required = false) String leaseOption,
            @RequestParam(required = false) Integer maxRoommates,

            // additional preferences
            @RequestParam(required = false) Boolean isPetFriendly,
            @RequestParam(required = false) String occupation,
            @RequestParam(required = false) Integer preferredMinAge,
            @RequestParam(required = false) Integer preferredMaxAge,
            @RequestParam(required = false) String guestFrequency,
            @RequestParam(required = false) String noiseTolerance,
            @RequestParam(required = false) String roommateLifestyle,
            @RequestParam(required = false) String roomTypePreference,

            // Pagination parameters
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) Integer size,

            Model model
    ) {
        // 1) Auth check
        if (principal == null) {
            return "redirect:/auth/login";
        }

        // Validate pagination parameters
        final int pageSize = paginationConfig.validatePageSize(size);
        final int pageNumber = (page != null && page >= 0) ? page : 0;

        // 2) Normalize blanks → null in final locals
        final String uniFilter = StringUtils.hasText(university) ? university.trim() : null;
        final String genderFilter = StringUtils.hasText(gender) ? gender.trim() : null;
        final String foodPrefFilter = StringUtils.hasText(foodPref) ? foodPref.trim() : null;
        final String alcoholPrefFilter = StringUtils.hasText(alcoholPref) ? alcoholPref.trim() : null;
        final Boolean smokingFilter = smoking;
        final Integer cleanlinessFilter = cleanlinessRating;
        final String sleepScheduleFilter = StringUtils.hasText(sleepSchedule) ? sleepSchedule.trim() : null;
        final String leaseOptionFilter = StringUtils.hasText(leaseOption) ? leaseOption.trim() : null;
        final Integer maxRoommatesFilter = maxRoommates;
        final Boolean petFriendlyFilter = isPetFriendly;
        final String occupationFilter = StringUtils.hasText(occupation) ? occupation.trim() : null;
        final Integer preferredMinAgeFilter = preferredMinAge;
        final Integer preferredMaxAgeFilter = preferredMaxAge;
        final String guestFrequencyFilter = StringUtils.hasText(guestFrequency) ? guestFrequency.trim() : null;
        final String noiseToleranceFilter = StringUtils.hasText(noiseTolerance) ? noiseTolerance.trim() : null;
        final String roommateLifestyleFilter = StringUtils.hasText(roommateLifestyle) ? roommateLifestyle.trim() : null;
        final String roomTypePreferenceFilter = StringUtils.hasText(roomTypePreference) ? roomTypePreference.trim() : null;

        // 3) Log incoming filter values
        logger.debug("Filters → uni={}, gen={}, food={}, alc={}, smoke={}, clean={}, sleep={}, lease={}, maxRm={}, pet={}, occ={}, minA={}, maxA={}, guest={}, noise={}, life={}, type={}, page={}, size={}",
                uniFilter, genderFilter, foodPrefFilter, alcoholPrefFilter, smokingFilter, cleanlinessFilter,
                sleepScheduleFilter, leaseOptionFilter, maxRoommatesFilter, petFriendlyFilter, occupationFilter,
                preferredMinAgeFilter, preferredMaxAgeFilter, guestFrequencyFilter, noiseToleranceFilter,
                roommateLifestyleFilter, roomTypePreferenceFilter, pageNumber, pageSize);

        // 4) Get current user
        UserEntity currentUser = userService.getUserByEmail(principal.getName());
        String currentUserEmail = currentUser.getEmail();

        // 5) Apply all filters to get filtered users
        List<UserEntity> filteredUsers = applyAllFilters(
                currentUser, uniFilter, genderFilter, foodPrefFilter, alcoholPrefFilter,
                smokingFilter, cleanlinessFilter, sleepScheduleFilter, leaseOptionFilter,
                maxRoommatesFilter, petFriendlyFilter, occupationFilter, preferredMinAgeFilter,
                preferredMaxAgeFilter, guestFrequencyFilter, noiseToleranceFilter,
                roommateLifestyleFilter, roomTypePreferenceFilter);

        // 6) Get all user preferences for displaying badges
        List<UserPreferences> allPreferences = userPreferencesService.getAllPreferences();

        // 7) Get total filtered count
        int totalCount = filteredUsers.size();

        // 8) Apply pagination
        List<UserEntity> paginatedUsers = applyPagination(filteredUsers, pageNumber, pageSize);

        // 9) Calculate total pages
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 10) Prepare model for view
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentUserEmail", currentUserEmail);
        model.addAttribute("users", paginatedUsers);
        model.addAttribute("userPreferences", allPreferences);
        model.addAttribute("totalCount", totalCount);
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("hasMore", pageNumber + 1 < totalPages);

        // 11) Add filter markers to model for form state preservation
        addFilterMarkersToModel(model, uniFilter, genderFilter, foodPrefFilter, alcoholPrefFilter,
                smokingFilter, cleanlinessFilter, sleepScheduleFilter, leaseOptionFilter,
                maxRoommatesFilter, petFriendlyFilter, occupationFilter, preferredMinAgeFilter,
                preferredMaxAgeFilter, guestFrequencyFilter, noiseToleranceFilter,
                roommateLifestyleFilter, roomTypePreferenceFilter);

        logger.debug("Returning view with userCount={} (total: {})", paginatedUsers.size(), totalCount);
        return "roommate-listings";
    }

    // REST endpoint for lazy loading more users
    @GetMapping("/roommate-listings/load-more")
    @ResponseBody
    public Map<String, Object> loadMoreRoommates(
            Principal principal,
            @RequestParam(required = false) String university,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String foodPref,
            @RequestParam(required = false) String alcoholPref,
            @RequestParam(required = false) Boolean smoking,
            @RequestParam(required = false) Integer cleanlinessRating,
            @RequestParam(required = false) String sleepSchedule,
            @RequestParam(required = false) String leaseOption,
            @RequestParam(required = false) Integer maxRoommates,
            @RequestParam(required = false) Boolean isPetFriendly,
            @RequestParam(required = false) String occupation,
            @RequestParam(required = false) Integer preferredMinAge,
            @RequestParam(required = false) Integer preferredMaxAge,
            @RequestParam(required = false) String guestFrequency,
            @RequestParam(required = false) String noiseTolerance,
            @RequestParam(required = false) String roommateLifestyle,
            @RequestParam(required = false) String roomTypePreference,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) Integer size) {

        logger.debug("Load more roommates request for page={}", page);

        // Validate page size
        final int pageSize = paginationConfig.validatePageSize(size);
        final int pageNumber = (page != null && page >= 0) ? page : 0;

        // Get current user
        UserEntity currentUser = userService.getUserByEmail(principal.getName());
        String currentUserEmail = currentUser.getEmail();

        // Apply all filters
        List<UserEntity> filteredUsers = applyAllFilters(
                currentUser, university, gender, foodPref, alcoholPref, smoking,
                cleanlinessRating, sleepSchedule, leaseOption, maxRoommates,
                isPetFriendly, occupation, preferredMinAge, preferredMaxAge,
                guestFrequency, noiseTolerance, roommateLifestyle, roomTypePreference);

        // Get total count
        int totalCount = filteredUsers.size();

        // Apply pagination
        List<UserEntity> paginatedUsers = applyPagination(filteredUsers, pageNumber, pageSize);

        // Get preferences for all users
        List<UserPreferences> allPreferences = userPreferencesService.getAllPreferences();
        Map<String, UserPreferences> preferencesMap = new HashMap<>();
        for (UserPreferences pref : allPreferences) {
            preferencesMap.put(pref.getUserId(), pref);
        }

        // Map user entities to DTOs for JSON response
        List<Map<String, Object>> userDTOs = paginatedUsers.stream()
                .map(user -> mapUserToDTO(user, preferencesMap.get(user.getId()), currentUserEmail))
                .collect(Collectors.toList());

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("users", userDTOs);
        response.put("totalCount", totalCount);
        response.put("currentPage", pageNumber);
        response.put("pageSize", pageSize);
        response.put("totalPages", totalPages);
        response.put("hasMore", pageNumber + 1 < totalPages);

        logger.debug("Returning {} users for page {}", userDTOs.size(), pageNumber);
        return response;
    }

    // Map user to DTO for JSON response
    private Map<String, Object> mapUserToDTO(UserEntity user, UserPreferences prefs, String currentUserEmail) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", user.getId());
        dto.put("firstName", user.getFirstName());
        dto.put("lastName", user.getLastName());
        dto.put("email", user.getEmail());
        dto.put("university", user.getUniversity());
        dto.put("bio", user.getBio());
        dto.put("phoneNumber", user.getPhoneNumber());
        dto.put("age", user.getAge());
        dto.put("createdAt", user.getCreatedAt());
        dto.put("profileImageData", user.getProfileImageData());
        dto.put("profileImageType", user.getProfileImageType());
        dto.put("base64Image", user.getBase64Image());
        dto.put("isCurrentUser", user.getEmail().equals(currentUserEmail));

        if (prefs != null) {
            Map<String, Object> prefsDto = new HashMap<>();
            prefsDto.put("gender", prefs.getGender());
            prefsDto.put("foodPref", prefs.getFoodPref());
            prefsDto.put("alcoholPref", prefs.getAlcoholPref());
            prefsDto.put("smoking", prefs.getSmoking());
            prefsDto.put("cleanlinessRating", prefs.getCleanlinessRating());
            prefsDto.put("sleepSchedule", prefs.getSleepSchedule());
            prefsDto.put("leaseOption", prefs.getLeaseOption());
            prefsDto.put("maxRoommates", prefs.getMaxRoommates());
            prefsDto.put("isPetFriendly", prefs.getIsPetFriendly());
            prefsDto.put("guestFrequency", prefs.getGuestFrequency());
            prefsDto.put("noiseTolerance", prefs.getNoiseTolerance());
            prefsDto.put("roommateLifestyle", prefs.getRoommateLifestyle());
            prefsDto.put("roomTypePreference", prefs.getRoomTypePreference());
            dto.put("preferences", prefsDto);
        }

        return dto;
    }

    // Helper method to apply all filters
    private List<UserEntity> applyAllFilters(UserEntity currentUser,
                                             String university, String gender, String foodPref,
                                             String alcoholPref, Boolean smoking, Integer cleanlinessRating,
                                             String sleepSchedule, String leaseOption, Integer maxRoommates,
                                             Boolean isPetFriendly, String occupation,
                                             Integer preferredMinAge, Integer preferredMaxAge,
                                             String guestFrequency, String noiseTolerance,
                                             String roommateLifestyle, String roomTypePreference) {

        // Normalize inputs
        final String uniFilter = StringUtils.hasText(university) ? university.trim() : null;
        final String genderFilter = StringUtils.hasText(gender) ? gender.trim() : null;
        final String foodPrefFilter = StringUtils.hasText(foodPref) ? foodPref.trim() : null;
        final String alcoholPrefFilter = StringUtils.hasText(alcoholPref) ? alcoholPref.trim() : null;
        final Boolean smokingFilter = smoking;
        final Integer cleanlinessFilter = cleanlinessRating;
        final String sleepScheduleFilter = StringUtils.hasText(sleepSchedule) ? sleepSchedule.trim() : null;
        final String leaseOptionFilter = StringUtils.hasText(leaseOption) ? leaseOption.trim() : null;
        final Integer maxRoommatesFilter = maxRoommates;
        final Boolean petFriendlyFilter = isPetFriendly;
        final String occupationFilter = StringUtils.hasText(occupation) ? occupation.trim() : null;
        final Integer preferredMinAgeFilter = preferredMinAge;
        final Integer preferredMaxAgeFilter = preferredMaxAge;
        final String guestFrequencyFilter = StringUtils.hasText(guestFrequency) ? guestFrequency.trim() : null;
        final String noiseToleranceFilter = StringUtils.hasText(noiseTolerance) ? noiseTolerance.trim() : null;
        final String roommateLifestyleFilter = StringUtils.hasText(roommateLifestyle) ? roommateLifestyle.trim() : null;
        final String roomTypePreferenceFilter = StringUtils.hasText(roomTypePreference) ? roomTypePreference.trim() : null;

        // Get all active users
        List<UserEntity> activeUsers = userService.findActiveUsers();

        // Get all preferences
        List<UserPreferences> allPreferences = userPreferencesService.getAllPreferences();

        // Exclude self
        activeUsers = activeUsers.stream()
                .filter(u -> !u.getEmail().equals(currentUser.getEmail()))
                .collect(Collectors.toList());

        // 1) University filter on UserEntity
        if (uniFilter != null) {
            activeUsers = activeUsers.stream()
                    .filter(u -> u.getUniversity() != null
                            && u.getUniversity().toLowerCase().contains(uniFilter.toLowerCase()))
                    .collect(Collectors.toList());
        }

        // 2) Age-based filtering directly on users
        if (preferredMinAgeFilter != null || preferredMaxAgeFilter != null) {
            activeUsers = activeUsers.stream()
                    .filter(user -> {
                        Byte userAge = user.getAge();
                        if (userAge == null) {
                            return false; // Skip users without age information
                        }

                        boolean matchesMinAge = preferredMinAgeFilter == null || userAge >= preferredMinAgeFilter;
                        boolean matchesMaxAge = preferredMaxAgeFilter == null || userAge <= preferredMaxAgeFilter;

                        return matchesMinAge && matchesMaxAge;
                    })
                    .collect(Collectors.toList());
        }

        // 3) Check if we only have age filters
        boolean onlyAgeFilters = (preferredMinAgeFilter != null || preferredMaxAgeFilter != null) &&
                !(genderFilter != null
                        || foodPrefFilter != null
                        || alcoholPrefFilter != null
                        || smokingFilter != null
                        || cleanlinessFilter != null
                        || sleepScheduleFilter != null
                        || leaseOptionFilter != null
                        || maxRoommatesFilter != null
                        || petFriendlyFilter != null
                        || occupationFilter != null
                        || guestFrequencyFilter != null
                        || noiseToleranceFilter != null
                        || roommateLifestyleFilter != null
                        || roomTypePreferenceFilter != null);

        // 4) If we only have age filters, skip preference-based filtering
        if (!onlyAgeFilters) {
            // Check if we have any preference filters
            boolean anyPrefFilter = genderFilter != null
                    || foodPrefFilter != null
                    || alcoholPrefFilter != null
                    || smokingFilter != null
                    || cleanlinessFilter != null
                    || sleepScheduleFilter != null
                    || leaseOptionFilter != null
                    || maxRoommatesFilter != null
                    || petFriendlyFilter != null
                    || occupationFilter != null
                    || guestFrequencyFilter != null
                    || noiseToleranceFilter != null
                    || roommateLifestyleFilter != null
                    || roomTypePreferenceFilter != null;

            if (anyPrefFilter) {
                // 5) Apply preference-based filters
                List<String> matchingIds = allPreferences.stream()
                        .filter(pref ->
                                (genderFilter == null || (pref.getGender() != null && pref.getGender().name().equalsIgnoreCase(genderFilter))) &&
                                        (foodPrefFilter == null || (pref.getFoodPref() != null && pref.getFoodPref().name().equalsIgnoreCase(foodPrefFilter))) &&
                                        (alcoholPrefFilter == null || (pref.getAlcoholPref() != null && pref.getAlcoholPref().name().equalsIgnoreCase(alcoholPrefFilter))) &&
                                        (smokingFilter == null || (pref.getSmoking() != null && pref.getSmoking().equals(smokingFilter))) &&
                                        (cleanlinessFilter == null || (pref.getCleanlinessRating() != null && pref.getCleanlinessRating().equals(cleanlinessFilter))) &&
                                        (sleepScheduleFilter == null || (pref.getSleepSchedule() != null && pref.getSleepSchedule().toLowerCase().contains(sleepScheduleFilter.toLowerCase()))) &&
                                        (leaseOptionFilter == null || (pref.getLeaseOption() != null && pref.getLeaseOption().name().equalsIgnoreCase(leaseOptionFilter))) &&
                                        (maxRoommatesFilter == null || (pref.getMaxRoommates() != null && pref.getMaxRoommates().equals(maxRoommatesFilter))) &&
                                        (petFriendlyFilter == null || (pref.getIsPetFriendly() != null && pref.getIsPetFriendly().equals(petFriendlyFilter))) &&
                                        (occupationFilter == null || (pref.getOccupation() != null && pref.getOccupation().name().equalsIgnoreCase(occupationFilter))) &&
                                        (guestFrequencyFilter == null || (pref.getGuestFrequency() != null && pref.getGuestFrequency().name().equalsIgnoreCase(guestFrequencyFilter))) &&
                                        (noiseToleranceFilter == null || (pref.getNoiseTolerance() != null && pref.getNoiseTolerance().name().equalsIgnoreCase(noiseToleranceFilter))) &&
                                        (roommateLifestyleFilter == null || (pref.getRoommateLifestyle() != null && pref.getRoommateLifestyle().name().equalsIgnoreCase(roommateLifestyleFilter))) &&
                                        (roomTypePreferenceFilter == null || (pref.getRoomTypePreference() != null && pref.getRoomTypePreference().name().equalsIgnoreCase(roomTypePreferenceFilter)))
                        )
                        .map(UserPreferences::getUserId)
                        .collect(Collectors.toList());

                // Filter users by matching preference IDs
                activeUsers = activeUsers.stream()
                        .filter(u -> matchingIds.contains(u.getId()))
                        .collect(Collectors.toList());
            }
        }

        return activeUsers;
    }

    // Helper method to add filter markers to model
    private void addFilterMarkersToModel(Model model, String uniFilter, String genderFilter,
                                         String foodPrefFilter, String alcoholPrefFilter,
                                         Boolean smokingFilter, Integer cleanlinessFilter,
                                         String sleepScheduleFilter, String leaseOptionFilter,
                                         Integer maxRoommatesFilter, Boolean petFriendlyFilter,
                                         String occupationFilter, Integer preferredMinAgeFilter,
                                         Integer preferredMaxAgeFilter, String guestFrequencyFilter,
                                         String noiseToleranceFilter, String roommateLifestyleFilter,
                                         String roomTypePreferenceFilter) {

        model.addAttribute("universityFilter", uniFilter);
        model.addAttribute("genderFilter", genderFilter);
        model.addAttribute("foodPrefFilter", foodPrefFilter);
        model.addAttribute("alcoholPrefFilter", alcoholPrefFilter);
        model.addAttribute("smokingFilter", smokingFilter);
        model.addAttribute("cleanlinessRatingFilter", cleanlinessFilter);
        model.addAttribute("sleepScheduleFilter", sleepScheduleFilter);
        model.addAttribute("leaseOptionFilter", leaseOptionFilter);
        model.addAttribute("maxRoommatesFilter", maxRoommatesFilter);
        model.addAttribute("isPetFriendlyFilter", petFriendlyFilter);
        model.addAttribute("occupationFilter", occupationFilter);
        model.addAttribute("preferredMinAgeFilter", preferredMinAgeFilter);
        model.addAttribute("preferredMaxAgeFilter", preferredMaxAgeFilter);
        model.addAttribute("guestFrequencyFilter", guestFrequencyFilter);
        model.addAttribute("noiseToleranceFilter", noiseToleranceFilter);
        model.addAttribute("roommateLifestyleFilter", roommateLifestyleFilter);
        model.addAttribute("roomTypePreferenceFilter", roomTypePreferenceFilter);
    }

    // Apartment Listings Page with advanced filtering
    @GetMapping("/apartment-listings")
    public String showApartmentListings(
            Principal principal,
            @RequestParam(required = false) Integer minRent,
            @RequestParam(required = false) Integer maxRent,
            @RequestParam(required = false) Integer minBeds,
            @RequestParam(required = false) Integer maxBeds,
            @RequestParam(required = false) Float minBaths,
            @RequestParam(required = false) Float maxBaths,
            @RequestParam(required = false) String locationKeyword,
            @RequestParam(required = false) Boolean isFurnished,
            @RequestParam(required = false) Boolean hasParking,
            @RequestParam(required = false) Boolean hasLaundry,
            @RequestParam(required = false) Boolean petsAllowed,
            @RequestParam(required = false) Integer minSquareFeet,
            @RequestParam(required = false) Integer maxSquareFeet,
            @RequestParam(required = false) Boolean subletAvailable,
            @RequestParam(required = false) String transportOptions,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableTo,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) Integer size,
            Model model) {

        if (principal != null) {
            // Validate pagination parameters
            final int pageSize = paginationConfig.validatePageSize(size);
            final int pageNumber = (page != null && page >= 0) ? page : 0;

            logger.debug("Apartment listings request with filters - page:{}, size:{}, sortBy:{}",
                    pageNumber, pageSize, sortBy);

            // Use the advanced filtering method from the service
            List<RoomInfo> filteredRooms = roomInfoService.findRoomsByAdvancedFilter(
                    minRent, maxRent, minBeds, maxBeds, minBaths, maxBaths,
                    locationKeyword, isFurnished, hasParking, hasLaundry, petsAllowed,
                    minSquareFeet, maxSquareFeet, subletAvailable, transportOptions,
                    availableFrom, availableTo
            );

            // Apply sorting if specified
            if (sortBy != null) {
                switch (sortBy) {
                    case "priceAsc":
                        filteredRooms = filteredRooms.stream()
                                .sorted(Comparator.comparing(RoomInfo::getRent, Comparator.nullsLast(Integer::compareTo)))
                                .collect(Collectors.toList());
                        break;
                    case "priceDesc":
                        filteredRooms = filteredRooms.stream()
                                .sorted(Comparator.comparing(RoomInfo::getRent, Comparator.nullsLast(Integer::compareTo)).reversed())
                                .collect(Collectors.toList());
                        break;
                    case "bedsDesc":
                        filteredRooms = filteredRooms.stream()
                                .sorted(Comparator.comparing(RoomInfo::getNumBeds, Comparator.nullsLast(Integer::compareTo)).reversed())
                                .collect(Collectors.toList());
                        break;
                    case "dateAsc":
                        filteredRooms = filteredRooms.stream()
                                .sorted(Comparator.comparing(RoomInfo::getAvailableFrom, Comparator.nullsLast(LocalDate::compareTo)))
                                .collect(Collectors.toList());
                        break;
                }
            }

            // Get total count for pagination
            int totalCount = filteredRooms.size();

            // Apply pagination to the filtered rooms
            List<RoomInfo> paginatedRooms = applyPagination(filteredRooms, pageNumber, pageSize);

            // Calculate total pages
            int totalPages = (int) Math.ceil((double) totalCount / pageSize);

            // Add rooms and pagination data to model
            model.addAttribute("rooms", paginatedRooms);
            model.addAttribute("totalCount", totalCount);
            model.addAttribute("currentPage", pageNumber);
            model.addAttribute("pageSize", pageSize);
            model.addAttribute("totalPages", totalPages);
            model.addAttribute("hasMore", pageNumber + 1 < totalPages);

            // Add filter parameters to model for maintaining state in the view
            model.addAttribute("minRentFilter", minRent);
            model.addAttribute("maxRentFilter", maxRent);
            model.addAttribute("minBedsFilter", minBeds);
            model.addAttribute("maxBedsFilter", maxBeds);
            model.addAttribute("minBathsFilter", minBaths);
            model.addAttribute("maxBathsFilter", maxBaths);
            model.addAttribute("locationFilter", locationKeyword);
            model.addAttribute("isFurnishedFilter", isFurnished);
            model.addAttribute("hasParkingFilter", hasParking);
            model.addAttribute("hasLaundryFilter", hasLaundry);
            model.addAttribute("petsAllowedFilter", petsAllowed);
            model.addAttribute("minSquareFeetFilter", minSquareFeet);
            model.addAttribute("maxSquareFeetFilter", maxSquareFeet);
            model.addAttribute("subletAvailableFilter", subletAvailable);
            model.addAttribute("transportOptionsFilter", transportOptions);
            model.addAttribute("availableFromFilter", availableFrom);
            model.addAttribute("availableToFilter", availableTo);
            model.addAttribute("sortByFilter", sortBy);

            logger.debug("Returning view with roomCount={} (total: {})", paginatedRooms.size(), totalCount);
            return "apartment-listings";
        }
        return "redirect:/auth/login";
    }

    // REST endpoint for loading more apartments
    @GetMapping("/apartment-listings/load-more")
    @ResponseBody
    public Map<String, Object> loadMoreApartments(
            Principal principal,
            @RequestParam(required = false) Integer minRent,
            @RequestParam(required = false) Integer maxRent,
            @RequestParam(required = false) Integer minBeds,
            @RequestParam(required = false) Integer maxBeds,
            @RequestParam(required = false) Float minBaths,
            @RequestParam(required = false) Float maxBaths,
            @RequestParam(required = false) String locationKeyword,
            @RequestParam(required = false) Boolean isFurnished,
            @RequestParam(required = false) Boolean hasParking,
            @RequestParam(required = false) Boolean hasLaundry,
            @RequestParam(required = false) Boolean petsAllowed,
            @RequestParam(required = false) Integer minSquareFeet,
            @RequestParam(required = false) Integer maxSquareFeet,
            @RequestParam(required = false) Boolean subletAvailable,
            @RequestParam(required = false) String transportOptions,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableTo,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false) Integer size) {

        logger.debug("Load more apartments request for page={}", page);

        // Validate page size
        final int pageSize = paginationConfig.validatePageSize(size);
        final int pageNumber = (page != null && page >= 0) ? page : 0;

        // Get filtered apartments
        List<RoomInfo> filteredRooms = roomInfoService.findRoomsByAdvancedFilter(
                minRent, maxRent, minBeds, maxBeds, minBaths, maxBaths,
                locationKeyword, isFurnished, hasParking, hasLaundry, petsAllowed,
                minSquareFeet, maxSquareFeet, subletAvailable, transportOptions,
                availableFrom, availableTo
        );

        // Apply sorting if specified
        if (sortBy != null) {
            switch (sortBy) {
                case "priceAsc":
                    filteredRooms = filteredRooms.stream()
                            .sorted(Comparator.comparing(RoomInfo::getRent, Comparator.nullsLast(Integer::compareTo)))
                            .collect(Collectors.toList());
                    break;
                case "priceDesc":
                    filteredRooms = filteredRooms.stream()
                            .sorted(Comparator.comparing(RoomInfo::getRent, Comparator.nullsLast(Integer::compareTo)).reversed())
                            .collect(Collectors.toList());
                    break;
                case "bedsDesc":
                    filteredRooms = filteredRooms.stream()
                            .sorted(Comparator.comparing(RoomInfo::getNumBeds, Comparator.nullsLast(Integer::compareTo)).reversed())
                            .collect(Collectors.toList());
                    break;
                case "dateAsc":
                    filteredRooms = filteredRooms.stream()
                            .sorted(Comparator.comparing(RoomInfo::getAvailableFrom, Comparator.nullsLast(LocalDate::compareTo)))
                            .collect(Collectors.toList());
                    break;
            }
        }

        // Get total count
        int totalCount = filteredRooms.size();

        // Apply pagination
        List<RoomInfo> paginatedRooms = applyPagination(filteredRooms, pageNumber, pageSize);

        // Map room entities to DTOs for JSON response
        List<Map<String, Object>> roomDTOs = paginatedRooms.stream()
                .map(this::mapRoomToDTO)
                .collect(Collectors.toList());

        // Calculate total pages
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // Build response
        Map<String, Object> response = new HashMap<>();
        response.put("rooms", roomDTOs);
        response.put("totalCount", totalCount);
        response.put("currentPage", pageNumber);
        response.put("pageSize", pageSize);
        response.put("totalPages", totalPages);
        response.put("hasMore", pageNumber + 1 < totalPages);

        logger.debug("Returning {} rooms for page {}", roomDTOs.size(), pageNumber);
        return response;
    }

    // Helper method to map RoomInfo to DTO for JSON response
    private Map<String, Object> mapRoomToDTO(RoomInfo room) {
        Map<String, Object> dto = new HashMap<>();
        dto.put("id", room.getId());
        dto.put("numBeds", room.getNumBeds());
        dto.put("numBaths", room.getNumBaths());
        dto.put("rent", room.getRent());
        dto.put("address", room.getAddress());
        dto.put("city", room.getCity());
        dto.put("state", room.getState());
        dto.put("zipCode", room.getZipCode());
        dto.put("description", room.getDescription());
        dto.put("availableFrom", room.getAvailableFrom());
        dto.put("isFurnished", room.getIsFurnished());
        dto.put("hasParking", room.getHasParking());
        dto.put("hasLaundry", room.getHasLaundry());
        dto.put("petsAllowed", room.getPetsAllowed());
        dto.put("squareFeet", room.getSquareFeet());
        dto.put("subletAvailable", room.getSubletAvailable());
        dto.put("transportOptions", room.getTransportOptions());
        dto.put("hasImage", room.hasImage());

        // Only include image data if it exists
        if (room.hasImage()) {
            dto.put("base64Image", room.getBase64Image());
            dto.put("imageType", room.getImageType());
        }

        return dto;
    }

    // Helper method to apply pagination (can be used by both roommate and apartment listings)
    private <T> List<T> applyPagination(List<T> items, int page, int size) {
        int fromIndex = page * size;

        if (items.size() <= fromIndex) {
            return Collections.emptyList();
        }

        int toIndex = Math.min(fromIndex + size, items.size());
        return items.subList(fromIndex, toIndex);
    }

    // Main listings page that redirects to roommate listings
    @GetMapping("/listings")
    public String showListings() {
        return "redirect:/home/roommate-listings";
    }

    @GetMapping("/boom")
    public String boom() {
        throw new RuntimeException("💥");
    }
}