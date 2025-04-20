package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.models.UserEntity;
import com.roomatefinder.demo.services.MessageService;
import com.roomatefinder.demo.services.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final MessageService messageService;
    private final UserService userService;

    public GlobalControllerAdvice(MessageService messageService, UserService userService) {
        this.messageService = messageService;
        this.userService = userService;
    }

    @ModelAttribute
    public void addCommonAttributes(Model model, Principal principal) {
        if (principal != null) {
            UserEntity user = userService.getUserByEmail(principal.getName());
            if (user != null) {
                int unreadCount = messageService.countUnreadMessages(user.getId());
                model.addAttribute("unreadMessageCount", unreadCount);
            }
        }
    }
}