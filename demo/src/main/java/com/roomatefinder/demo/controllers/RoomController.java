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
//            @RequestParam("rentNegotiable") boolean rentNegotiable,
            @RequestParam(name="rentNegotiable", required=false)
            Boolean rentNegotiable,
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
            @RequestParam(name="rentNegotiable", required=false)
            Boolean rentNegotiable,
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
