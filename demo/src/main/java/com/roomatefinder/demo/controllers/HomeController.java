//package com.roomatefinder.demo.controllers;
//
//import com.roomatefinder.demo.models.UserEntity;
//import com.roomatefinder.demo.models.RoomInfo;
//import com.roomatefinder.demo.models.UserPreferences;
//import com.roomatefinder.demo.services.UserPreferencesService;
//import com.roomatefinder.demo.services.UserService;
//import com.roomatefinder.demo.services.RoomInfoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import java.security.Principal;
//import java.util.List;
//import java.util.stream.Collectors;
//import org.springframework.transaction.annotation.Transactional;
//
//@Controller
//@RequestMapping("/home")
//public class HomeController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RoomInfoService roomInfoService;
//
//    @Autowired
//    private UserPreferencesService userPreferencesService;
//
//    // Dashboard view
//    @GetMapping("/dashboard")
//    public String showDashboard(Principal principal, Model model) {
//        if (principal != null) {
//            UserEntity user = userService.getUserByEmail(principal.getName());
//            model.addAttribute("user", user);
//        }
//        return "dashboard";
//    }
//
//    // REST API endpoints
//    @GetMapping("/users")
//    @ResponseBody
//    public List<UserEntity> getUsers() {
//        System.out.println("getting current users");
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/current-user")
//    @ResponseBody
//    public String getLoggedInUser(Principal principal) {
//        return principal.getName();
//    }
//
//    // Roommate Listings Page
//    @GetMapping("/roommate-listings")
//    @Transactional(readOnly = true)
//    public String showRoommateListings(Principal principal, Model model) {
//        if (principal != null) {
//            // Get active users
//            List<UserEntity> activeUsers = userService.findActiveUsers();
//            model.addAttribute("users", activeUsers);
//
//            // Get all preferences and log data for debugging
//            List<UserPreferences> allPreferences = userPreferencesService.getAllPreferences();
//            System.out.println("Found " + allPreferences.size() + " total preferences");
//
//            // Log user IDs for matching purposes
//            for (UserEntity user : activeUsers) {
//                System.out.println("User ID: " + user.getId() + ", Email: " + user.getEmail());
//            }
//
//            // Log preferences for verification
//            for (UserPreferences pref : allPreferences) {
//                System.out.println("Preference for user ID: " + pref.getUserId());
//            }
//
//            model.addAttribute("userPreferences", allPreferences);
//
//            return "roommate-listings";
//        }
//        return "redirect:/auth/login";
//    }
//
//    // Apartment Listings Page with filtering
//    @GetMapping("/apartment-listings")
//    public String showApartmentListings(
//            Principal principal,
//            @RequestParam(required = false) Integer maxRent,
//            @RequestParam(required = false) Integer minBeds,
//            @RequestParam(required = false) String locationKeyword,
//            Model model) {
//
//        if (principal != null) {
//            List<RoomInfo> availableRooms = roomInfoService.findAvailableRooms();
//
//            // Apply filters if provided
//            if (maxRent != null || minBeds != null || locationKeyword != null) {
//                availableRooms = availableRooms.stream()
//                        .filter(room -> maxRent == null || room.getRent() <= maxRent)
//                        .filter(room -> minBeds == null || room.getNumBeds() >= minBeds)
//                        .filter(room -> locationKeyword == null ||
//                                (room.getCity() != null && room.getCity().toLowerCase().contains(locationKeyword.toLowerCase())) ||
//                                (room.getState() != null && room.getState().toLowerCase().contains(locationKeyword.toLowerCase())) ||
//                                (room.getAddress() != null && room.getAddress().toLowerCase().contains(locationKeyword.toLowerCase())))
//                        .collect(Collectors.toList());
//            }
//
//            model.addAttribute("rooms", availableRooms);
//            return "apartment-listings";
//        }
//        return "redirect:/auth/login";
//    }
//
//    // Main listings page that redirects to roommate listings
//    @GetMapping("/listings")
//    public String showListings() {
//        return "redirect:/home/roommate-listings";
//    }
//}
//package com.roomatefinder.demo.controllers;
//
//import com.roomatefinder.demo.models.UserEntity;
//import com.roomatefinder.demo.models.RoomInfo;
//import com.roomatefinder.demo.models.UserPreferences;
//import com.roomatefinder.demo.services.UserPreferencesService;
//import com.roomatefinder.demo.services.UserService;
//import com.roomatefinder.demo.services.RoomInfoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//import java.security.Principal;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Controller
//@RequestMapping("/home")
//public class HomeController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private RoomInfoService roomInfoService;
//
//    @Autowired
//    private UserPreferencesService userPreferencesService;
//
//    // Dashboard view
//    @GetMapping("/dashboard")
//    public String showDashboard(Principal principal, Model model) {
//        if (principal != null) {
//            UserEntity user = userService.getUserByEmail(principal.getName());
//            model.addAttribute("user", user);
//        }
//        return "dashboard";
//    }
//
//    // REST API endpoints
//    @GetMapping("/users")
//    @ResponseBody
//    public List<UserEntity> getUsers() {
//        System.out.println("getting current users");
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/current-user")
//    @ResponseBody
//    public String getLoggedInUser(Principal principal) {
//        return principal.getName();
//    }
//
//    // Roommate Listings Page with filtering
//    @GetMapping("/roommate-listings")
//    public String showRoommateListings(
//            Principal principal,
//            @RequestParam(required = false) String university,
//            @RequestParam(required = false) String gender,
//            @RequestParam(required = false) Integer minAge,
//            @RequestParam(required = false) Integer maxAge,
//            @RequestParam(required = false) Boolean smokerFriendly,
//            @RequestParam(required = false) Boolean petFriendly,
//            @RequestParam(required = false) Boolean partyFriendly,
//            @RequestParam(required = false) Integer minBudget,
//            @RequestParam(required = false) Integer maxBudget,
//            @RequestParam(required = false) Integer cleanlinessLevel,
//            Model model) {
//
//        if (principal != null) {
//            // Get active users
//            List<UserEntity> activeUsers = userService.findActiveUsers();
//
//            // Get current user
//            UserEntity currentUser = userService.getUserByEmail(principal.getName());
//            model.addAttribute("currentUser", currentUser);
//
//            // Get all preferences
//            List<UserPreferences> allPreferences = userPreferencesService.getAllPreferences();
//            System.out.println("Found " + allPreferences.size() + " total preferences");
//
//            // Apply filters if provided
//            if (university != null || gender != null || minAge != null || maxAge != null ||
//                    smokerFriendly != null || petFriendly != null || partyFriendly != null ||
//                    minBudget != null || maxBudget != null || cleanlinessLevel != null) {
//
//                // Filter users by university
//                if (university != null && !university.isEmpty()) {
//                    activeUsers = activeUsers.stream()
//                            .filter(user -> user.getUniversity() != null &&
//                                    user.getUniversity().toLowerCase().contains(university.toLowerCase()))
//                            .collect(Collectors.toList());
//                }
//
//                // Further filter results by user preferences
//                if (gender != null || minAge != null || maxAge != null ||
//                        smokerFriendly != null || petFriendly != null || partyFriendly != null ||
//                        minBudget != null || maxBudget != null || cleanlinessLevel != null) {
//
//                    // Get user IDs with preferences that match the filter criteria
//                    List<String> matchingUserIds = allPreferences.stream()
//                            .filter(pref ->
//                                    (gender == null || (pref.getPreferredGender() != null &&
//                                            pref.getPreferredGender().equalsIgnoreCase(gender))) &&
//                                            (minAge == null || (pref.getMinAge() != null && pref.getMinAge() >= minAge)) &&
//                                            (maxAge == null || (pref.getMaxAge() != null && pref.getMaxAge() <= maxAge)) &&
//                                            (smokerFriendly == null || (pref.getIsSmokerFriendly() != null &&
//                                                    pref.getIsSmokerFriendly().equals(smokerFriendly))) &&
//                                            (petFriendly == null || (pref.getIsPetFriendly() != null &&
//                                                    pref.getIsPetFriendly().equals(petFriendly))) &&
//                                            (partyFriendly == null || (pref.getIsPartyFriendly() != null &&
//                                                    pref.getIsPartyFriendly().equals(partyFriendly))) &&
//                                            (minBudget == null || (pref.getMaxBudget() != null && pref.getMaxBudget() >= minBudget)) &&
//                                            (maxBudget == null || (pref.getMinBudget() != null && pref.getMinBudget() <= maxBudget)) &&
//                                            (cleanlinessLevel == null || (pref.getCleanlinessLevel() != null &&
//                                                    pref.getCleanlinessLevel() >= cleanlinessLevel))
//                            )
//                            .map(UserPreferences::getUserId)
//                            .collect(Collectors.toList());
//
//                    // Filter users based on matching preferences
//                    activeUsers = activeUsers.stream()
//                            .filter(user -> matchingUserIds.contains(user.getId()))
//                            .collect(Collectors.toList());
//                }
//            }
//
//            // Exclude current user from the list
//            activeUsers = activeUsers.stream()
//                    .filter(user -> !user.getEmail().equals(principal.getName()))
//                    .collect(Collectors.toList());
//
//            model.addAttribute("users", activeUsers);
//            model.addAttribute("userPreferences", allPreferences);
//
//            // Add filter parameters to model for maintaining state in the view
//            model.addAttribute("universityFilter", university);
//            model.addAttribute("genderFilter", gender);
//            model.addAttribute("minAgeFilter", minAge);
//            model.addAttribute("maxAgeFilter", maxAge);
//            model.addAttribute("smokerFriendlyFilter", smokerFriendly);
//            model.addAttribute("petFriendlyFilter", petFriendly);
//            model.addAttribute("partyFriendlyFilter", partyFriendly);
//            model.addAttribute("minBudgetFilter", minBudget);
//            model.addAttribute("maxBudgetFilter", maxBudget);
//            model.addAttribute("cleanlinessLevelFilter", cleanlinessLevel);
//
//            return "roommate-listings";
//        }
//        return "redirect:/auth/login";
//    }
//
//    // Apartment Listings Page with filtering
//    @GetMapping("/apartment-listings")
//    public String showApartmentListings(
//            Principal principal,
//            @RequestParam(required = false) Integer maxRent,
//            @RequestParam(required = false) Integer minBeds,
//            @RequestParam(required = false) String locationKeyword,
//            Model model) {
//
//        if (principal != null) {
//            List<RoomInfo> availableRooms = roomInfoService.findAvailableRooms();
//
//            // Apply filters if provided
//            if (maxRent != null || minBeds != null || locationKeyword != null) {
//                availableRooms = availableRooms.stream()
//                        .filter(room -> maxRent == null || room.getRent() <= maxRent)
//                        .filter(room -> minBeds == null || room.getNumBeds() >= minBeds)
//                        .filter(room -> locationKeyword == null ||
//                                (room.getCity() != null && room.getCity().toLowerCase().contains(locationKeyword.toLowerCase())) ||
//                                (room.getState() != null && room.getState().toLowerCase().contains(locationKeyword.toLowerCase())) ||
//                                (room.getAddress() != null && room.getAddress().toLowerCase().contains(locationKeyword.toLowerCase())))
//                        .collect(Collectors.toList());
//            }
//
//            model.addAttribute("rooms", availableRooms);
//            model.addAttribute("maxRentFilter", maxRent);
//            model.addAttribute("minBedsFilter", minBeds);
//            model.addAttribute("locationFilter", locationKeyword);
//
//            return "apartment-listings";
//        }
//        return "redirect:/auth/login";
//    }
//
//    // Main listings page that redirects to roommate listings
//    @GetMapping("/listings")
//    public String showListings() {
//        return "redirect:/home/roommate-listings";
//    }
//}
package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.models.UserEntity;
import com.roomatefinder.demo.models.RoomInfo;
import com.roomatefinder.demo.models.UserPreferences;
import com.roomatefinder.demo.services.UserPreferencesService;
import com.roomatefinder.demo.services.UserService;
import com.roomatefinder.demo.services.RoomInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import java.security.Principal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoomInfoService roomInfoService;

    @Autowired
    private UserPreferencesService userPreferencesService;

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
        System.out.println("getting current users");
        return userService.getAllUsers();
    }

    @GetMapping("/current-user")
    @ResponseBody
    public String getLoggedInUser(Principal principal) {
        return principal.getName();
    }

    // Roommate Listings Page with filtering
    @GetMapping("/roommate-listings")
    public String showRoommateListings(
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
            Model model) {

        if (principal != null) {
            // Get current user
            UserEntity currentUser = userService.getUserByEmail(principal.getName());
            model.addAttribute("currentUser", currentUser);

            // Get active users
            List<UserEntity> activeUsers = userService.findActiveUsers();

            // Get all preferences
            List<UserPreferences> allPreferences = userPreferencesService.getAllPreferences();
            System.out.println("Found " + allPreferences.size() + " total preferences");

            // Apply filters if provided
            if (university != null || gender != null || foodPref != null || alcoholPref != null ||
                    smoking != null || cleanlinessRating != null || sleepSchedule != null ||
                    leaseOption != null || maxRoommates != null) {

                // Filter users by university
                if (university != null && !university.isEmpty()) {
                    activeUsers = activeUsers.stream()
                            .filter(user -> user.getUniversity() != null &&
                                    user.getUniversity().toLowerCase().contains(university.toLowerCase()))
                            .collect(Collectors.toList());
                }

                // Further filter results by user preferences
                if (gender != null || foodPref != null || alcoholPref != null || smoking != null ||
                        cleanlinessRating != null || sleepSchedule != null || leaseOption != null || maxRoommates != null) {

                    // Get user IDs with preferences that match the filter criteria
                    List<String> matchingUserIds = allPreferences.stream()
                            .filter(pref ->
                                    (gender == null || (pref.getGender() != null &&
                                            pref.getGender().name().equalsIgnoreCase(gender))) &&
                                            (foodPref == null || (pref.getFoodPref() != null &&
                                                    pref.getFoodPref().name().equalsIgnoreCase(foodPref))) &&
                                            (alcoholPref == null || (pref.getAlcoholPref() != null &&
                                                    pref.getAlcoholPref().name().equalsIgnoreCase(alcoholPref))) &&
                                            (smoking == null || (pref.getSmoking() != null &&
                                                    pref.getSmoking().equals(smoking))) &&
                                            (cleanlinessRating == null || (pref.getCleanlinessRating() != null &&
                                                    pref.getCleanlinessRating() >= cleanlinessRating)) &&
                                            (sleepSchedule == null || (pref.getSleepSchedule() != null &&
                                                    pref.getSleepSchedule().toLowerCase().contains(sleepSchedule.toLowerCase()))) &&
                                            (leaseOption == null || (pref.getLeaseOption() != null &&
                                                    pref.getLeaseOption().name().equalsIgnoreCase(leaseOption))) &&
                                            (maxRoommates == null || (pref.getMaxRoommates() != null &&
                                                    pref.getMaxRoommates() <= maxRoommates))
                            )
                            .map(UserPreferences::getUserId)
                            .collect(Collectors.toList());

                    // Filter users based on matching preferences
                    activeUsers = activeUsers.stream()
                            .filter(user -> matchingUserIds.contains(user.getId()))
                            .collect(Collectors.toList());
                }
            }

            // Exclude current user from the list
            activeUsers = activeUsers.stream()
                    .filter(user -> !user.getEmail().equals(principal.getName()))
                    .collect(Collectors.toList());

            model.addAttribute("users", activeUsers);
            model.addAttribute("userPreferences", allPreferences);

            // Add filter parameters to model for maintaining state in the view
            model.addAttribute("universityFilter", university);
            model.addAttribute("genderFilter", gender);
            model.addAttribute("foodPrefFilter", foodPref);
            model.addAttribute("alcoholPrefFilter", alcoholPref);
            model.addAttribute("smokingFilter", smoking);
            model.addAttribute("cleanlinessRatingFilter", cleanlinessRating);
            model.addAttribute("sleepScheduleFilter", sleepSchedule);
            model.addAttribute("leaseOptionFilter", leaseOption);
            model.addAttribute("maxRoommatesFilter", maxRoommates);

            return "roommate-listings";
        }
        return "redirect:/auth/login";
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
            Model model) {

        if (principal != null) {
            // Use the advanced filtering method from the service
            List<RoomInfo> availableRooms = roomInfoService.findRoomsByAdvancedFilter(
                    minRent, maxRent, minBeds, maxBeds, minBaths, maxBaths,
                    locationKeyword, isFurnished, hasParking, hasLaundry, petsAllowed,
                    minSquareFeet, maxSquareFeet, subletAvailable, transportOptions,
                    availableFrom, availableTo
            );

            // Apply sorting if specified
            if (sortBy != null) {
                switch (sortBy) {
                    case "priceAsc":
                        availableRooms = availableRooms.stream()
                                .sorted(Comparator.comparing(RoomInfo::getRent, Comparator.nullsLast(Integer::compareTo)))
                                .collect(Collectors.toList());
                        break;
                    case "priceDesc":
                        availableRooms = availableRooms.stream()
                                .sorted(Comparator.comparing(RoomInfo::getRent, Comparator.nullsLast(Integer::compareTo)).reversed())
                                .collect(Collectors.toList());
                        break;
                    case "bedsDesc":
                        availableRooms = availableRooms.stream()
                                .sorted(Comparator.comparing(RoomInfo::getNumBeds, Comparator.nullsLast(Integer::compareTo)).reversed())
                                .collect(Collectors.toList());
                        break;
                    case "dateAsc":
                        availableRooms = availableRooms.stream()
                                .sorted(Comparator.comparing(RoomInfo::getAvailableFrom, Comparator.nullsLast(LocalDate::compareTo)))
                                .collect(Collectors.toList());
                        break;
                }
            }

            model.addAttribute("rooms", availableRooms);

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

            return "apartment-listings";
        }
        return "redirect:/auth/login";
    }

    // Main listings page that redirects to roommate listings
    @GetMapping("/listings")
    public String showListings() {
        return "redirect:/home/roommate-listings";
    }

    // University specific rooms
    @GetMapping("/university-apartments")
    public String showUniversityApartments(
            Principal principal,
            @RequestParam(required = true) String university,
            @RequestParam(required = false) Integer maxDistance,
            @RequestParam(required = false) Integer maxRent,
            Model model) {

        if (principal == null) {
            return "redirect:/auth/login";
        }

        List<RoomInfo> universityRooms = roomInfoService.findRoomsByUniversityProximity(
                university, maxDistance);

        // Additional filtering by rent if requested
        if (maxRent != null) {
            universityRooms = universityRooms.stream()
                    .filter(room -> room.getRent() <= maxRent)
                    .collect(Collectors.toList());
        }

        model.addAttribute("rooms", universityRooms);
        model.addAttribute("universityFilter", university);
        model.addAttribute("maxDistanceFilter", maxDistance);
        model.addAttribute("maxRentFilter", maxRent);

        return "university-apartments";
    }
}