package com.roomatefinder.demo.security;

import com.roomatefinder.demo.services.TokenBlacklistService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final Set<String> PUBLIC_ENDPOINTS = Set.of(
            "/auth/register",
            "/auth/login",
            "/auth/refresh-token",
            "/error",                // Add error endpoint
            "/favicon.ico",          // Add favicon
            "/webjars/**",           // For static resources
            "/css/**",               // CSS files
            "/js/**"                 // JavaScript files
    );

    @Autowired private JwtHelper jwtHelper;
    @Autowired private UserDetailsService userDetailsService;
    @Autowired private TokenBlacklistService tokenBlacklistService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        try {

            // Skip JWT validation if session authentication exists
            if (SecurityContextHolder.getContext().getAuthentication() != null &&
                    SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
                chain.doFilter(request, response);
                return;
            }

            String header = request.getHeader("Authorization");

            // If no Authorization header, continue the chain without setting authentication
            if (header == null || !header.startsWith("Bearer ")) {
                chain.doFilter(request, response);
                return;
            }

            if (shouldSkipAuthentication(request)) {
                chain.doFilter(request, response);
                return;
            }

            String token = extractJwtToken(request);
            if (token == null) {
                sendError(response, "Authorization header missing");
                return;
            }

            if (tokenBlacklistService.isTokenBlacklisted(token)) {
                sendError(response, "Token revoked");
                return;
            }

            processJwtToken(request, token);
            chain.doFilter(request, response);

        } catch (ExpiredJwtException ex) {
            handleJwtException(response, "Token expired", ex, HttpStatus.UNAUTHORIZED);
        } catch (MalformedJwtException | SignatureException | IllegalArgumentException ex) {
            handleJwtException(response, "Invalid token", ex, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            handleJwtException(response, "Authentication failed", ex, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean shouldSkipAuthentication(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return PUBLIC_ENDPOINTS.stream().anyMatch(requestURI::startsWith);
    }

    private String extractJwtToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }

    private void processJwtToken(HttpServletRequest request, String token) {
        String username = jwtHelper.getUsernameFromToken(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtHelper.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                authentication.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
    }

    private void sendError(HttpServletResponse response, String message)
            throws IOException {
        logger.error("JWT Error: {}", message);
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().write(message);
        response.getWriter().flush();
    }

    private void handleJwtException(HttpServletResponse response,
                                    String message,
                                    Exception ex,
                                    HttpStatus status)
            throws IOException {
        logger.error("JWT Exception: {} - {}", message, ex.getMessage());
        response.sendError(status.value(), message);
    }
}
