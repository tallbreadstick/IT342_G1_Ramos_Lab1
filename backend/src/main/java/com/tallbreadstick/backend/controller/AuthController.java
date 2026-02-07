package com.tallbreadstick.backend.controller;

import com.tallbreadstick.backend.dto.AuthResponse;
import com.tallbreadstick.backend.dto.LoginRequest;
import com.tallbreadstick.backend.dto.RegisterRequest;
import com.tallbreadstick.backend.entity.User;
import com.tallbreadstick.backend.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // =====================
    // POST /api/auth/register
    // =====================
    @PostMapping("/auth/register")
    public ResponseEntity<User> register(
            @RequestBody RegisterRequest request,
            HttpServletRequest httpRequest
    ) {
        String ip = httpRequest.getRemoteAddr();
        User user = authService.register(request, ip);
        return ResponseEntity.status(201).body(user);
    }

    // =====================
    // POST /api/auth/login
    // =====================
    @PostMapping("/auth/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ) {
        String ip = httpRequest.getRemoteAddr();
        AuthResponse response = authService.login(request, ip);
        return ResponseEntity.ok(response);
    }

    // =====================
    // GET /api/user/me (PROTECTED)
    // =====================
    @GetMapping("/user/me")
    public ResponseEntity<User> getCurrentUser(
            @RequestHeader("Authorization") String authHeader
    ) {
        String token = extractToken(authHeader);
        User user = authService.getCurrentUser(token);
        return ResponseEntity.ok(user);
    }

    // =====================
    // POST /api/auth/logout (OPTIONAL)
    // =====================
    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(
            @RequestHeader("Authorization") String authHeader,
            HttpServletRequest httpRequest
    ) {
        String token = extractToken(authHeader);
        String ip = httpRequest.getRemoteAddr();

        authService.logout(token, ip);
        return ResponseEntity.ok().build();
    }

    // =====================
    // Helper
    // =====================
    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        return authHeader.substring(7);
    }
}
