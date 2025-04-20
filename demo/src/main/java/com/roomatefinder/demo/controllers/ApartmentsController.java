package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.models.RoomInfo;
import com.roomatefinder.demo.models.UserEntity;
import com.roomatefinder.demo.services.RoomInfoService;
import com.roomatefinder.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/apartments")
@RequiredArgsConstructor
public class ApartmentsController {

    private final RoomInfoService roomInfoService;
    private final UserService userService;

    @GetMapping("/details/{id}")
    public String showApartmentDetails(@PathVariable Long id, Principal principal, Model model) {
        // Check if user is logged in
        if (principal == null) {
            return "redirect:/auth/login";
        }

        // Get the room details
        RoomInfo room = roomInfoService.getRoomById(id);
        if (room == null) {
            return "redirect:/home/apartment-listings?error=not_found";
        }

        // Get the room owner's information
        UserEntity owner = userService.getUserById(room.getUserId());
        if (owner == null) {
            // Handle case where owner could not be found
            model.addAttribute("ownerNotFound", true);
        } else {
            model.addAttribute("owner", owner);
        }

        // Add room data to model
        model.addAttribute("room", room);

        // Get current user to check if they are the owner
        UserEntity currentUser = userService.getUserByEmail(principal.getName());
        boolean isOwner = currentUser != null && currentUser.getId().equals(room.getUserId());
        model.addAttribute("isOwner", isOwner);


        if (room.getVideoUrl() != null && room.getVideoUrl().contains("youtube.com/watch?v=")) {
            String embedUrl = room.getVideoUrl().replace("youtube.com/watch?v=", "youtube.com/embed/");
            room.setVideoUrl(embedUrl);
        }
        return "apartment-details";
    }
}