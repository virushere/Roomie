package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.models.Message;
import com.roomatefinder.demo.models.RoomInfo;
import com.roomatefinder.demo.models.UserEntity;
import com.roomatefinder.demo.services.MessageService;
import com.roomatefinder.demo.services.RoomInfoService;
import com.roomatefinder.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;
    private final RoomInfoService roomInfoService;

    @Autowired
    public MessageController(MessageService messageService, UserService userService, RoomInfoService roomInfoService) {
        this.messageService = messageService;
        this.userService = userService;
        this.roomInfoService = roomInfoService;
    }

    @GetMapping("/compose")
    public String showComposeForm(@RequestParam(required = false) String recipient, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        if (recipient != null && !recipient.isEmpty()) {
            UserEntity recipientUser = userService.getUserById(recipient);
            if (recipientUser != null) {
                model.addAttribute("recipient", recipientUser);
            }
        }

        return "message-compose";
    }

    @PostMapping("/send")
    public String sendMessage(
            @RequestParam("recipientId") String recipientId,
            @RequestParam("message") String messageContent,
            @RequestParam(value = "roomId", required = false) Long roomId,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (principal == null) {
            return "redirect:/auth/login";
        }

        try {
            // Get current user
            UserEntity sender = userService.getUserByEmail(principal.getName());

            // Create new message
            Message message = new Message();
            message.setSenderId(sender.getId());
            message.setRecipientId(recipientId);
            message.setContent(messageContent);
            message.setSentAt(LocalDateTime.now());
            message.setRead(false);

            // Set room reference if provided
            if (roomId != null) {
                message.setRoomId(roomId);
            }

            // Save message
            messageService.saveMessage(message);

            // Add success message
            redirectAttributes.addFlashAttribute("success", "Message sent successfully!");

            // Redirect to inbox
            return "redirect:/messages/inbox";
        } catch (Exception e) {
            // Add error message
            redirectAttributes.addFlashAttribute("error", "Failed to send message: " + e.getMessage());

            // Redirect back to compose form with recipient ID
            return "redirect:/messages/compose?recipient=" + recipientId;
        }
    }

    @GetMapping("/inbox")
    public String viewInbox(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        UserEntity currentUser = userService.getUserByEmail(principal.getName());
        List<Message> receivedMessages = messageService.getReceivedMessages(currentUser.getId());

        model.addAttribute("messages", receivedMessages);
        return "message-inbox";
    }

    @GetMapping("/sent")
    public String viewSentMessages(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        UserEntity currentUser = userService.getUserByEmail(principal.getName());
        List<Message> sentMessages = messageService.getSentMessages(currentUser.getId());

        model.addAttribute("messages", sentMessages);
        return "message-sent";
    }

    @GetMapping("/view/{id}")
    public String viewMessage(@PathVariable Long id, Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/auth/login";
        }

        try {
            // Get current user
            UserEntity currentUser = userService.getUserByEmail(principal.getName());

            // Get message
            Message message = messageService.findById(id)
                    .orElseThrow(() -> new RuntimeException("Message not found"));

            // Check if user is authorized to view this message
            if (!message.getSenderId().equals(currentUser.getId()) &&
                    !message.getRecipientId().equals(currentUser.getId())) {
                throw new RuntimeException("You are not authorized to view this message");
            }

            // Determine if this is an inbox message (received) or sent message
            boolean isInbox = message.getRecipientId().equals(currentUser.getId());
            model.addAttribute("isInbox", isInbox);

            // Populate sender and recipient information
            if (message.getSender() == null) {
                UserEntity sender = userService.getUserById(message.getSenderId());
                message.setSender(sender);
            }

            if (message.getRecipient() == null) {
                UserEntity recipient = userService.getUserById(message.getRecipientId());
                message.setRecipient(recipient);
            }

            // If room ID is set, populate room information
            if (message.getRoomId() != null && message.getRoom() == null) {
                try {
                    RoomInfo room = roomInfoService.getRoomById(message.getRoomId());
                    message.setRoom(room);
                } catch (Exception e) {
                    // Room might have been deleted
                    System.err.println("Could not load room: " + e.getMessage());
                }
            }

            // If this is an inbox message and it's not read, mark it as read
            if (isInbox && !message.getRead()) {
                messageService.markAsRead(message.getId());
                message.setRead(true);
            }

            model.addAttribute("message", message);
            return "message-view";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/messages/inbox?error=" + e.getMessage();
        }
    }

    // Helper method needed in the controller since we removed the direct repository access
    private Message findMessageById(Long id) {
        return messageService.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }
}