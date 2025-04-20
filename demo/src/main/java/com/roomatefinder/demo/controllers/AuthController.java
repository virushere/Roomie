package com.roomatefinder.demo.controllers;

import com.roomatefinder.demo.models.JwtRequest;
import com.roomatefinder.demo.models.JwtResponse;
import com.roomatefinder.demo.security.JwtHelper;
import com.roomatefinder.demo.services.CustomUserDetailsService;
import com.roomatefinder.demo.services.TokenBlacklistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtHelper jwtHelper;
    private final TokenBlacklistService tokenBlacklistService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager,
                          CustomUserDetailsService customUserDetailsService,
                          JwtHelper jwtHelper,
                          TokenBlacklistService tokenBlacklistService) {
        this.authenticationManager = authenticationManager;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtHelper = jwtHelper;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    // Login Form Handling
    @GetMapping("/login")
    public String showLoginForm(HttpSession session) {
        if (session.getAttribute("token") != null) {
            return "redirect:/home/listings";
        }
        return "login";
    }

    @PostMapping("/login-form")
    public String processFormLogin(@RequestParam String email,
                                   @RequestParam String password,
                                   HttpSession session,
                                   Model model) {
        try {
            System.out.println("Login attempt for user: " + email);
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate and store the token
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            String token = jwtHelper.generateToken(userDetails);

            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                model.addAttribute("errorMessage", "Session limit exceeded");
                return "login";
            }

            session.setAttribute("token", token);
            session.setAttribute("user", userDetails.getUsername());

            System.out.println("Authentication successful, redirecting to listings page");

            // Redirect to listings page
            return "redirect:/home/listings";

        } catch (BadCredentialsException e) {
            System.out.println("Authentication failed: " + e.getMessage());
            model.addAttribute("errorMessage", "Invalid username or password");
            return "login";
        }
    }

    // REST API Authentication
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> jwtLogin(@RequestBody JwtRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtHelper.generateToken(userDetails);

            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Session limit exceeded");
            }

            return ResponseEntity.ok(JwtResponse.builder()
                    .jwtToken(token)
                    .userName(userDetails.getUsername())
                    .build());

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    // Logout Handling
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<?> apiLogout(@RequestHeader("Authorization") String tokenHeader) {
        String token = extractJwtToken(tokenHeader);
        if (token != null) {
            tokenBlacklistService.blacklistToken(token);
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok("Logged out successfully");
    }

    @GetMapping("/logout")
    public String webLogout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String token = (String) session.getAttribute("token");
            if (token != null) {
                tokenBlacklistService.blacklistToken(token);
            }
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return "redirect:/auth/login?logout=true";
    }

    private String extractJwtToken(String header) {
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
