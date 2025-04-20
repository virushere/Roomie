//package com.roomatefinder.demo.controllers;
//
//import com.roomatefinder.demo.models.RoomInfo;
//import com.roomatefinder.demo.models.UserEntity;
//import com.roomatefinder.demo.services.RoomInfoService;
//import com.roomatefinder.demo.services.UserService;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import jakarta.servlet.http.HttpServletRequest;
//
//import java.io.IOException;
//import java.time.LocalDate;
//import java.util.Arrays;
//
//@Controller
//@RequestMapping("/rooms")
//@RequiredArgsConstructor
//@Slf4j
//public class RoomController {
//
//    private final RoomInfoService roomInfoService;
//    private final UserService userService;
//
//    // Show create room form
//    @GetMapping("/create")
//    public String showCreateRoomForm(Model model) {
//        model.addAttribute("room", new RoomInfo());
//        model.addAttribute("today", LocalDate.now());
//        return "create-room";
//    }
//
//    // Process create room form
//    @PostMapping("/create")
//    public String createRoom(
//            @RequestParam("rent") int rent,
//            @RequestParam("rentNegotiable") boolean rentNegotiable,
//            @RequestParam("utilitiesCost") int utilitiesCost,
//            @RequestParam("subletAvailable") boolean subletAvailable,
//            @RequestParam("transportOptions") String transportOptions,
//            @RequestParam("numBeds") int numBeds,
//            @RequestParam("currentOccupancy") int currentOccupancy,
//            @RequestParam("totalCapacity") int totalCapacity,
//            @RequestParam("availableFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableFrom,
//            @RequestParam(value = "availableUntil", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableUntil,
//            @RequestParam(value = "roomImageFile", required = false) MultipartFile roomImageFile,
//            @RequestParam(value = "videoUrl", required = false) String videoUrl,
//            @RequestParam(value = "description", required = false) String description,
//            @RequestParam(value = "address", required = false) String address,
//            @RequestParam(value = "city", required = false) String city,
//            @RequestParam(value = "state", required = false) String state,
//            @RequestParam(value = "zipCode", required = false) String zipCode,
//            @RequestParam(value = "numBaths", required = false) Float numBaths,
//            @RequestParam(value = "squareFeet", required = false) Integer squareFeet,
//            @RequestParam(value = "isFurnished", required = false, defaultValue = "false") boolean isFurnished,
//            @RequestParam(value = "hasParking", required = false, defaultValue = "false") boolean hasParking,
//            @RequestParam(value = "hasLaundry", required = false, defaultValue = "false") boolean hasLaundry,
//            @RequestParam(value = "petsAllowed", required = false, defaultValue = "false") boolean petsAllowed,
//            @RequestParam(value = "nearbyUniversities", required = false) String nearbyUniversities,
//            @RequestParam(value = "distanceToUniversity", required = false) Integer distanceToUniversity,
//            @AuthenticationPrincipal UserDetails userDetails,
//            Model model) {
//
////        // Debug logging
////        log.debug("Create Room - Received form data:");
////        log.debug("New fields - numBaths: {}, squareFeet: {}", numBaths, squareFeet);
////        log.debug("New fields - isFurnished: {}, hasParking: {}, hasLaundry: {}, petsAllowed: {}",
////                isFurnished, hasParking, hasLaundry, petsAllowed);
////        log.debug("New fields - nearbyUniversities: {}, distanceToUniversity: {}",
////                nearbyUniversities, distanceToUniversity);
//
//        // Validate inputs
//        if (totalCapacity < currentOccupancy) {
//            model.addAttribute("error", "Total capacity must be greater than or equal to current occupancy");
//            model.addAttribute("today", LocalDate.now());
//            return "create-room";
//        }
//
//        // Get the current user
//        UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());
//
//        // Create a new room object
//        RoomInfo room = new RoomInfo();
//        room.setUserId(currentUser.getId());
//        room.setRent(rent);
//        room.setRentNegotiable(rentNegotiable);
//        room.setUtilitiesCost(utilitiesCost);
//        room.setSubletAvailable(subletAvailable);
//        room.setTransportOptions(transportOptions);
//        room.setNumBeds(numBeds);
//        room.setCurrentOccupancy(currentOccupancy);
//        room.setTotalCapacity(totalCapacity);
//        room.setAvailableFrom(availableFrom);
//        room.setAvailableUntil(availableUntil);
//
//        // Set optional fields
//        if (videoUrl != null && !videoUrl.trim().isEmpty()) {
//            room.setVideoUrl(videoUrl);
//        }
//        if (description != null && !description.trim().isEmpty()) {
//            room.setDescription(description);
//        }
//        if (address != null && !address.trim().isEmpty()) {
//            room.setAddress(address);
//        }
//        if (city != null && !city.trim().isEmpty()) {
//            room.setCity(city);
//        }
//        if (state != null && !state.trim().isEmpty()) {
//            room.setState(state);
//        }
//        if (zipCode != null && !zipCode.trim().isEmpty()) {
//            room.setZipCode(zipCode);
//        }
//
//        // Set new fields
//        room.setNumBaths(numBaths);
//        room.setSquareFeet(squareFeet);
//        room.setIsFurnished(isFurnished);
//        room.setHasParking(hasParking);
//        room.setHasLaundry(hasLaundry);
//        room.setPetsAllowed(petsAllowed);
//
//        if (nearbyUniversities != null && !nearbyUniversities.trim().isEmpty()) {
//            room.setNearbyUniversities(nearbyUniversities);
//        }
//
//        room.setDistanceToUniversity(distanceToUniversity);
//
//        // Handle image upload
//        if (roomImageFile != null && !roomImageFile.isEmpty()) {
//            try {
//                String originalFilename = roomImageFile.getOriginalFilename();
//                String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
//
//                room.setImageData(roomImageFile.getBytes());
//                room.setImageType(roomImageFile.getContentType());
//                room.setImageFilename(uniqueFilename);
//
//                log.debug("Room image stored: {}", uniqueFilename);
//                log.debug("Image type: {}", room.getImageType());
//                log.debug("Image size: {} bytes", room.getImageData() != null ? room.getImageData().length : 0);
//
//            } catch (IOException e) {
//                log.error("Failed to upload image", e);
//                model.addAttribute("error", "Failed to upload image");
//                model.addAttribute("today", LocalDate.now());
//                return "create-room";
//            }
//        }
//
//        // Save the room
//        try {
//            RoomInfo savedRoom = roomInfoService.createRoom(room);
//            log.debug("Room created with ID: {}", savedRoom.getId());
//            log.debug("Saved room details - numBaths: {}, squareFeet: {}", savedRoom.getNumBaths(), savedRoom.getSquareFeet());
//            log.debug("Saved room details - isFurnished: {}, hasParking: {}, hasLaundry: {}, petsAllowed: {}",
//                    savedRoom.getIsFurnished(), savedRoom.getHasParking(), savedRoom.getHasLaundry(), savedRoom.getPetsAllowed());
//            return "redirect:/users/my-listings?success=room_created";
//        } catch (Exception e) {
//            log.error("Failed to create room", e);
//            model.addAttribute("error", "Failed to create room: " + e.getMessage());
//            model.addAttribute("today", LocalDate.now());
//            return "create-room";
//        }
//    }
//
//    // Show edit room form
//    @GetMapping("/edit/{id}")
//    public String showEditRoomForm(
//            @PathVariable Long id,
//            Model model,
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        RoomInfo room = roomInfoService.getRoomById(id);
//        UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());
//
//        // Check if the room belongs to the current user
//        if (!room.getUserId().equals(currentUser.getId())) {
//            return "redirect:/users/my-listings?error=unauthorized";
//        }
//
//        // Debug log the room details before sending to view
//        log.debug("Edit Room - Found room: {}", room.getId());
//        log.debug("Room details - numBaths: {}, squareFeet: {}", room.getNumBaths(), room.getSquareFeet());
//        log.debug("Room details - isFurnished: {}, hasParking: {}, hasLaundry: {}, petsAllowed: {}",
//                room.getIsFurnished(), room.getHasParking(), room.getHasLaundry(), room.getPetsAllowed());
//        log.debug("Room details - nearbyUniversities: {}, distanceToUniversity: {}",
//                room.getNearbyUniversities(), room.getDistanceToUniversity());
//
//        model.addAttribute("room", room);
//        model.addAttribute("today", LocalDate.now());
//        return "edit-room";
//    }
//
//    // Process edit room form
//    @PostMapping("/edit/{id}")
//    public String updateRoom(
//            @PathVariable Long id,
//            @RequestParam("rent") int rent,
//            @RequestParam("rentNegotiable") boolean rentNegotiable,
//            @RequestParam("utilitiesCost") int utilitiesCost,
//            @RequestParam("subletAvailable") boolean subletAvailable,
//            @RequestParam("transportOptions") String transportOptions,
//            @RequestParam("numBeds") int numBeds,
//            @RequestParam("currentOccupancy") int currentOccupancy,
//            @RequestParam("totalCapacity") int totalCapacity,
//            @RequestParam("availableFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableFrom,
//            @RequestParam(value = "availableUntil", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableUntil,
//            @RequestParam(value = "roomImageFile", required = false) MultipartFile roomImageFile,
//            @RequestParam(value = "videoUrl", required = false) String videoUrl,
//            @RequestParam(value = "description", required = false) String description,
//            @RequestParam(value = "address", required = false) String address,
//            @RequestParam(value = "city", required = false) String city,
//            @RequestParam(value = "state", required = false) String state,
//            @RequestParam(value = "zipCode", required = false) String zipCode,
//            @RequestParam(value = "numBaths", required = false) Float numBaths,
//            @RequestParam(value = "squareFeet", required = false) Integer squareFeet,
//            @RequestParam(value = "isFurnished", required = false, defaultValue = "false") boolean isFurnished,
//            @RequestParam(value = "hasParking", required = false, defaultValue = "false") boolean hasParking,
//            @RequestParam(value = "hasLaundry", required = false, defaultValue = "false") boolean hasLaundry,
//            @RequestParam(value = "petsAllowed", required = false, defaultValue = "false") boolean petsAllowed,
//            @RequestParam(value = "nearbyUniversities", required = false) String nearbyUniversities,
//            @RequestParam(value = "distanceToUniversity", required = false) Integer distanceToUniversity,
//            HttpServletRequest request,  // Added HttpServletRequest to access raw parameters
//            @AuthenticationPrincipal UserDetails userDetails,
//            Model model) {
//
//        // Debug logging
//        log.debug("Update Room - Received form data for room ID: {}", id);
//        log.debug("New fields - numBaths: {}, squareFeet: {}", numBaths, squareFeet);
//
//        // Log raw request parameters to see exactly what's being sent from the form
//        log.debug("RAW REQUEST PARAMETERS:");
//        log.debug("isFurnished parameter values: {}", Arrays.toString(request.getParameterValues("isFurnished")));
//        log.debug("hasParking parameter values: {}", Arrays.toString(request.getParameterValues("hasParking")));
//        log.debug("hasLaundry parameter values: {}", Arrays.toString(request.getParameterValues("hasLaundry")));
//        log.debug("petsAllowed parameter values: {}", Arrays.toString(request.getParameterValues("petsAllowed")));
//
//        // Log raw form values as received by @RequestParam
//        log.debug("REQUESTPARAM VALUES - isFurnished: {}, hasParking: {}, hasLaundry: {}, petsAllowed: {}",
//                isFurnished, hasParking, hasLaundry, petsAllowed);
//
//        log.debug("New fields - nearbyUniversities: {}, distanceToUniversity: {}",
//                nearbyUniversities, distanceToUniversity);
//
//        // Validate inputs
//        if (totalCapacity < currentOccupancy) {
//            model.addAttribute("error", "Total capacity must be greater than or equal to current occupancy");
//            model.addAttribute("today", LocalDate.now());
//            return "edit-room";
//        }
//
//        // Get the existing room
//        RoomInfo existingRoom = roomInfoService.getRoomById(id);
//        UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());
//
//        // Check if the room belongs to the current user
//        if (!existingRoom.getUserId().equals(currentUser.getId())) {
//            return "redirect:/users/my-listings?error=unauthorized";
//        }
//
//        // Current boolean values before update
//        log.debug("CURRENT BOOLEAN VALUES - isFurnished: {}, hasParking: {}, hasLaundry: {}, petsAllowed: {}",
//                existingRoom.getIsFurnished(), existingRoom.getHasParking(),
//                existingRoom.getHasLaundry(), existingRoom.getPetsAllowed());
//
//        // Update the room properties
//        existingRoom.setRent(rent);
//        existingRoom.setRentNegotiable(rentNegotiable);
//        existingRoom.setUtilitiesCost(utilitiesCost);
//        existingRoom.setSubletAvailable(subletAvailable);
//        existingRoom.setTransportOptions(transportOptions);
//        existingRoom.setNumBeds(numBeds);
//        existingRoom.setCurrentOccupancy(currentOccupancy);
//        existingRoom.setTotalCapacity(totalCapacity);
//        existingRoom.setAvailableFrom(availableFrom);
//        existingRoom.setAvailableUntil(availableUntil);
//
//        // Update optional fields
//        existingRoom.setVideoUrl(videoUrl);
//        existingRoom.setDescription(description);
//        existingRoom.setAddress(address);
//        existingRoom.setCity(city);
//        existingRoom.setState(state);
//        existingRoom.setZipCode(zipCode);
//
//        // Update new fields
//        existingRoom.setNumBaths(numBaths);
//        existingRoom.setSquareFeet(squareFeet);
//
//        // OPTION 1: Use the regular approach with form values
//        existingRoom.setIsFurnished(isFurnished);
//        existingRoom.setHasParking(hasParking);
//        existingRoom.setHasLaundry(hasLaundry);
//        existingRoom.setPetsAllowed(petsAllowed);
//
//        // OPTION 2: Force all boolean fields to true (uncomment to use)
//    /*
//    existingRoom.setIsFurnished(true);
//    existingRoom.setHasParking(true);
//    existingRoom.setHasLaundry(true);
//    existingRoom.setPetsAllowed(true);
//    log.debug("FORCING all boolean properties to TRUE");
//    */
//
//        existingRoom.setNearbyUniversities(nearbyUniversities);
//        existingRoom.setDistanceToUniversity(distanceToUniversity);
//
//        // Log the state of the room before saving
//        log.debug("Room about to be updated - numBaths: {}, squareFeet: {}",
//                existingRoom.getNumBaths(), existingRoom.getSquareFeet());
//        log.debug("Room about to be updated - isFurnished: {}, hasParking: {}, hasLaundry: {}, petsAllowed: {}",
//                existingRoom.getIsFurnished(), existingRoom.getHasParking(), existingRoom.getHasLaundry(), existingRoom.getPetsAllowed());
//
//        // Additional detailed logging about property values
//        log.debug("Boolean primitive values - isFurnished: {}, hasParking: {}, hasLaundry: {}, petsAllowed: {}",
//                isFurnished, hasParking, hasLaundry, petsAllowed);
//        log.debug("Entity boolean object values - isFurnished: {}, hasParking: {}, hasLaundry: {}, petsAllowed: {}",
//                existingRoom.getIsFurnished(), existingRoom.getHasParking(), existingRoom.getHasLaundry(), existingRoom.getPetsAllowed());
//
//        // Handle image upload if provided
//        if (roomImageFile != null && !roomImageFile.isEmpty()) {
//            try {
//                String originalFilename = roomImageFile.getOriginalFilename();
//                String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
//
//                existingRoom.setImageData(roomImageFile.getBytes());
//                existingRoom.setImageType(roomImageFile.getContentType());
//                existingRoom.setImageFilename(uniqueFilename);
//                log.debug("Updated room with new image: {}", uniqueFilename);
//
//            } catch (IOException e) {
//                log.error("Failed to upload image", e);
//                model.addAttribute("error", "Failed to upload image");
//                model.addAttribute("room", existingRoom);
//                model.addAttribute("today", LocalDate.now());
//                return "edit-room";
//            }
//        }
//
//        // Save the updated room
//        try {
//            RoomInfo updatedRoom = roomInfoService.updateRoom(existingRoom);
//            log.debug("Room updated successfully with ID: {}", updatedRoom.getId());
//            log.debug("Updated room details - numBaths: {}, squareFeet: {}",
//                    updatedRoom.getNumBaths(), updatedRoom.getSquareFeet());
//            log.debug("Updated room details - isFurnished: {}, hasParking: {}, hasLaundry: {}, petsAllowed: {}",
//                    updatedRoom.getIsFurnished(), updatedRoom.getHasParking(), updatedRoom.getHasLaundry(), updatedRoom.getPetsAllowed());
//
//            // Verify the update with a fresh entity from the database
//            RoomInfo verifiedRoom = roomInfoService.getRoomById(id);
//            log.debug("VERIFIED from database - Room ID: {}, isFurnished={}, hasParking={}, hasLaundry={}, petsAllowed={}",
//                    verifiedRoom.getId(),
//                    verifiedRoom.getIsFurnished(),
//                    verifiedRoom.getHasParking(),
//                    verifiedRoom.getHasLaundry(),
//                    verifiedRoom.getPetsAllowed());
//
//            return "redirect:/users/my-listings?success=room_updated";
//        } catch (Exception e) {
//            log.error("Failed to update room", e);
//            model.addAttribute("error", "Failed to update room: " + e.getMessage());
//            model.addAttribute("room", existingRoom);
//            model.addAttribute("today", LocalDate.now());
//            return "edit-room";
//        }
//    }
//
//    // Delete room
//    @PostMapping("/delete/{id}")
//    public String deleteRoom(
//            @PathVariable Long id,
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        try {
//            RoomInfo room = roomInfoService.getRoomById(id);
//            UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());
//
//            // Check if the room belongs to the current user
//            if (!room.getUserId().equals(currentUser.getId())) {
//                return "redirect:/users/my-listings?error=unauthorized";
//            }
//
//            roomInfoService.deleteRoom(id);
//            log.debug("Room deleted with ID: {}", id);
//            return "redirect:/users/my-listings?success=room_deleted";
//        } catch (RuntimeException e) {
//            log.error("Failed to delete room", e);
//            return "redirect:/users/my-listings?error=delete_failed";
//        }
//    }
//
//    // Endpoint to serve room images
//    @GetMapping("/image/{id}")
//    @ResponseBody
//    public ResponseEntity<byte[]> getRoomImage(@PathVariable Long id) {
//        RoomInfo room = roomInfoService.getRoomById(id);
//        if (room != null && room.getImageData() != null) {
//            HttpHeaders headers = new HttpHeaders();
//            headers.setContentType(MediaType.parseMediaType(room.getImageType()));
//            return new ResponseEntity<>(room.getImageData(), headers, HttpStatus.OK);
//        }
//        return ResponseEntity.notFound().build();
//    }
//}

package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.models.RoomInfo;
import com.roomatefinder.demo.models.UserEntity;
import com.roomatefinder.demo.services.RoomInfoService;
import com.roomatefinder.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;

@Controller
@RequestMapping("/rooms")
@RequiredArgsConstructor
@Slf4j
public class RoomController {

    private final RoomInfoService roomInfoService;
    private final UserService userService;

    // Show create room form
    @GetMapping("/create")
    public String showCreateRoomForm(Model model) {
        model.addAttribute("room", new RoomInfo());
        model.addAttribute("today", LocalDate.now());
        return "create-room";
    }

    // Process create room form
    @PostMapping("/create")
    public String createRoom(
            @RequestParam("rent") int rent,
            @RequestParam("rentNegotiable") boolean rentNegotiable,
            @RequestParam("utilitiesCost") int utilitiesCost,
            @RequestParam(value = "subletAvailable", required = false, defaultValue = "false") boolean subletAvailable,
            @RequestParam("transportOptions") String transportOptions,
            @RequestParam("numBeds") int numBeds,
            @RequestParam("currentOccupancy") int currentOccupancy,
            @RequestParam("totalCapacity") int totalCapacity,
            @RequestParam("availableFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableFrom,
            @RequestParam(value = "availableUntil", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableUntil,
            @RequestParam(value = "roomImageFile", required = false) MultipartFile roomImageFile,
            @RequestParam(value = "videoUrl", required = false) String videoUrl,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "zipCode", required = false) String zipCode,
            @RequestParam(value = "numBaths", required = false) Float numBaths,
            @RequestParam(value = "squareFeet", required = false) Integer squareFeet,
            // Change to Boolean (wrapper) so that a missing value is null, not false by default.
            @RequestParam(value = "isFurnished", required = false) Boolean isFurnished,
            @RequestParam(value = "hasParking", required = false) Boolean hasParking,
            @RequestParam(value = "hasLaundry", required = false) Boolean hasLaundry,
            @RequestParam(value = "petsAllowed", required = false) Boolean petsAllowed,
            @RequestParam(value = "nearbyUniversities", required = false) String nearbyUniversities,
            @RequestParam(value = "distanceToUniversity", required = false) Integer distanceToUniversity,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        // Validate capacity
        if (totalCapacity < currentOccupancy) {
            model.addAttribute("error", "Total capacity must be greater than or equal to current occupancy");
            model.addAttribute("today", LocalDate.now());
            return "create-room";
        }

        // Get the current user
        UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());

        // Create a new room object
        RoomInfo room = new RoomInfo();
        room.setUserId(currentUser.getId());
        room.setRent(rent);
        room.setRentNegotiable(rentNegotiable);
        room.setUtilitiesCost(utilitiesCost);
        room.setSubletAvailable(subletAvailable);
        room.setTransportOptions(transportOptions);
        room.setNumBeds(numBeds);
        room.setCurrentOccupancy(currentOccupancy);
        room.setTotalCapacity(totalCapacity);
        room.setAvailableFrom(availableFrom);
        room.setAvailableUntil(availableUntil);

        // Set optional fields
        if (videoUrl != null && !videoUrl.trim().isEmpty()) {
            room.setVideoUrl(videoUrl);
        }
        if (description != null && !description.trim().isEmpty()) {
            room.setDescription(description);
        }
        if (address != null && !address.trim().isEmpty()) {
            room.setAddress(address);
        }
        if (city != null && !city.trim().isEmpty()) {
            room.setCity(city);
        }
        if (state != null && !state.trim().isEmpty()) {
            room.setState(state);
        }
        if (zipCode != null && !zipCode.trim().isEmpty()) {
            room.setZipCode(zipCode);
        }

        // Set new fields
        room.setNumBaths(numBaths);
        room.setSquareFeet(squareFeet);
        // Use explicit null-check. If the value is missing, default to false.
        room.setIsFurnished(isFurnished != null ? isFurnished : false);
        room.setHasParking(hasParking != null ? hasParking : false);
        room.setHasLaundry(hasLaundry != null ? hasLaundry : false);
        room.setPetsAllowed(petsAllowed != null ? petsAllowed : false);

        if (nearbyUniversities != null && !nearbyUniversities.trim().isEmpty()) {
            room.setNearbyUniversities(nearbyUniversities);
        }
        room.setDistanceToUniversity(distanceToUniversity);

        // Handle image upload
        if (roomImageFile != null && !roomImageFile.isEmpty()) {
            try {
                String originalFilename = roomImageFile.getOriginalFilename();
                String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
                room.setImageData(roomImageFile.getBytes());
                room.setImageType(roomImageFile.getContentType());
                room.setImageFilename(uniqueFilename);
//                log.debug("Room image stored: {}", uniqueFilename);
            } catch (IOException e) {
                log.error("Failed to upload image", e);
                model.addAttribute("error", "Failed to upload image");
                model.addAttribute("today", LocalDate.now());
                return "create-room";
            }
        }

        // Save the room
        try {
            RoomInfo savedRoom = roomInfoService.createRoom(room);
//            log.debug("Room created with ID: {}", savedRoom.getId());
            return "redirect:/users/my-listings?success=room_created";
        } catch (Exception e) {
            log.error("Failed to create room", e);
            model.addAttribute("error", "Failed to create room: " + e.getMessage());
            model.addAttribute("today", LocalDate.now());
            return "create-room";
        }
    }

    // Show edit room form
    @GetMapping("/edit/{id}")
    public String showEditRoomForm(@PathVariable Long id,
                                   Model model,
                                   @AuthenticationPrincipal UserDetails userDetails) {
        RoomInfo room = roomInfoService.getRoomById(id);
        UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());
        if (!room.getUserId().equals(currentUser.getId())) {
            return "redirect:/users/my-listings?error=unauthorized";
        }
        log.debug("Edit Room - Found room: {}", room.getId());
        model.addAttribute("room", room);
        model.addAttribute("today", LocalDate.now());
        return "edit-room";
    }

    // Process edit room form
    @PostMapping("/edit/{id}")
    public String updateRoom(
            @PathVariable Long id,
            @RequestParam("rent") int rent,
            @RequestParam("rentNegotiable") boolean rentNegotiable,
            @RequestParam("utilitiesCost") int utilitiesCost,
            @RequestParam("subletAvailable") boolean subletAvailable,
            @RequestParam("transportOptions") String transportOptions,
            @RequestParam("numBeds") int numBeds,
            @RequestParam("currentOccupancy") int currentOccupancy,
            @RequestParam("totalCapacity") int totalCapacity,
            @RequestParam("availableFrom") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableFrom,
            @RequestParam(value = "availableUntil", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate availableUntil,
            @RequestParam(value = "roomImageFile", required = false) MultipartFile roomImageFile,
            @RequestParam(value = "videoUrl", required = false) String videoUrl,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "address", required = false) String address,
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "state", required = false) String state,
            @RequestParam(value = "zipCode", required = false) String zipCode,
            @RequestParam(value = "numBaths", required = false) Float numBaths,
            @RequestParam(value = "squareFeet", required = false) Integer squareFeet,
            @RequestParam(value = "isFurnished", required = false) Boolean isFurnished,
            @RequestParam(value = "hasParking", required = false) Boolean hasParking,
            @RequestParam(value = "hasLaundry", required = false) Boolean hasLaundry,
            @RequestParam(value = "petsAllowed", required = false) Boolean petsAllowed,
            @RequestParam(value = "nearbyUniversities", required = false) String nearbyUniversities,
            @RequestParam(value = "distanceToUniversity", required = false) Integer distanceToUniversity,
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        if (totalCapacity < currentOccupancy) {
            model.addAttribute("error", "Total capacity must be greater than or equal to current occupancy");
            model.addAttribute("today", LocalDate.now());
            return "edit-room";
        }

        RoomInfo existingRoom = roomInfoService.getRoomById(id);
        UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());
        if (!existingRoom.getUserId().equals(currentUser.getId())) {
            return "redirect:/users/my-listings?error=unauthorized";
        }

        // Update the room properties
        existingRoom.setRent(rent);
        existingRoom.setRentNegotiable(rentNegotiable);
        existingRoom.setUtilitiesCost(utilitiesCost);
        existingRoom.setSubletAvailable(subletAvailable);
        existingRoom.setTransportOptions(transportOptions);
        existingRoom.setNumBeds(numBeds);
        existingRoom.setCurrentOccupancy(currentOccupancy);
        existingRoom.setTotalCapacity(totalCapacity);
        existingRoom.setAvailableFrom(availableFrom);
        existingRoom.setAvailableUntil(availableUntil);
        existingRoom.setVideoUrl(videoUrl);
        existingRoom.setDescription(description);
        existingRoom.setAddress(address);
        existingRoom.setCity(city);
        existingRoom.setState(state);
        existingRoom.setZipCode(zipCode);
        existingRoom.setNumBaths(numBaths);
        existingRoom.setSquareFeet(squareFeet);

        // Set booleans with explicit default values if null
        existingRoom.setIsFurnished(isFurnished != null ? isFurnished : false);
        existingRoom.setHasParking(hasParking != null ? hasParking : false);
        existingRoom.setHasLaundry(hasLaundry != null ? hasLaundry : false);
        existingRoom.setPetsAllowed(petsAllowed != null ? petsAllowed : false);

        existingRoom.setNearbyUniversities(nearbyUniversities);
        existingRoom.setDistanceToUniversity(distanceToUniversity);

        // Handle image upload if provided
        if (roomImageFile != null && !roomImageFile.isEmpty()) {
            try {
                String originalFilename = roomImageFile.getOriginalFilename();
                String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
                existingRoom.setImageData(roomImageFile.getBytes());
                existingRoom.setImageType(roomImageFile.getContentType());
                existingRoom.setImageFilename(uniqueFilename);
//                log.debug("Updated room with new image: {}", uniqueFilename);
            } catch (IOException e) {
                log.error("Failed to upload image", e);
                model.addAttribute("error", "Failed to upload image");
                model.addAttribute("room", existingRoom);
                model.addAttribute("today", LocalDate.now());
                return "edit-room";
            }
        }

        try {
            RoomInfo updatedRoom = roomInfoService.updateRoom(existingRoom);
//            log.debug("Room updated successfully with ID: {}", updatedRoom.getId());
            return "redirect:/users/my-listings?success=room_updated";
        } catch (Exception e) {
            log.error("Failed to update room", e);
            model.addAttribute("error", "Failed to update room: " + e.getMessage());
            model.addAttribute("room", existingRoom);
            model.addAttribute("today", LocalDate.now());
            return "edit-room";
        }
    }

    // Delete room
    @PostMapping("/delete/{id}")
    public String deleteRoom(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails userDetails) {
        try {
            RoomInfo room = roomInfoService.getRoomById(id);
            UserEntity currentUser = userService.getUserByEmail(userDetails.getUsername());
            if (!room.getUserId().equals(currentUser.getId())) {
                return "redirect:/users/my-listings?error=unauthorized";
            }
            roomInfoService.deleteRoom(id);
            log.debug("Room deleted with ID: {}", id);
            return "redirect:/users/my-listings?success=room_deleted";
        } catch (RuntimeException e) {
            log.error("Failed to delete room", e);
            return "redirect:/users/my-listings?error=delete_failed";
        }
    }

    // Endpoint to serve room images
    @GetMapping("/image/{id}")
    @ResponseBody
    public ResponseEntity<byte[]> getRoomImage(@PathVariable Long id) {
        RoomInfo room = roomInfoService.getRoomById(id);
        if (room != null && room.getImageData() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(room.getImageType()));
            return new ResponseEntity<>(room.getImageData(), headers, HttpStatus.OK);
        }
        return ResponseEntity.notFound().build();
    }
}
