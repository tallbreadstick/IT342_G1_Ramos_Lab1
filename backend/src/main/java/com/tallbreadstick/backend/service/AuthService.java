package com.tallbreadstick.backend.service;

import com.tallbreadstick.backend.dto.*;
import com.tallbreadstick.backend.entity.AuthLog;
import com.tallbreadstick.backend.entity.User;
import com.tallbreadstick.backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TokenService tokenService;
    private final AuthLogService authLogService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(
            UserRepository userRepository,
            TokenService tokenService,
            AuthLogService authLogService
    ) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.authLogService = authLogService;
    }

    public User register(RegisterRequest request, String ip) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);

        authLogService.log(AuthLog.Action.REGISTER, user, AuthLog.Status.OK, ip);

        return user;
    }

    public AuthResponse login(LoginRequest request, String ip) {
        // Find user by username or email
        System.out.println("[Login Attempt] user input: " + request.getUsernameOrEmail());
        System.out.println("[Login Attempt] raw password: " + request.getPassword());

        User user = userRepository.findByUsername(request.getUsernameOrEmail())
                .orElseGet(() -> userRepository.findByEmail(request.getUsernameOrEmail())
                        .orElseThrow(() -> {
                            System.out.println("[Login Failed] user not found");
                            return new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
                        }));

        System.out.println("[Login Attempt] stored hash: " + user.getPassword());
        System.out.println("[Login Attempt] password matches? " + passwordEncoder.matches(request.getPassword(), user.getPassword()));

        // Verify password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            authLogService.log(AuthLog.Action.LOGIN, user, AuthLog.Status.ERR, ip);
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        // Generate JWT token
        String token = tokenService.generateToken(user);

        // Log successful login
        authLogService.log(AuthLog.Action.LOGIN, user, AuthLog.Status.OK, ip);

        // Return response
        return new AuthResponse(token, user);
    }

    public void logout(String token, String ip) {
        Integer userId = tokenService.extractUserId(token);
        User user = userRepository.findById(userId)
                .orElseThrow();

        tokenService.invalidateToken(token);
        authLogService.log(AuthLog.Action.LOGOUT, user, AuthLog.Status.OK, ip);
    }

    public User getCurrentUser(String token) {
        Integer userId = tokenService.extractUserId(token);
        return userRepository.findById(userId).orElseThrow();
    }
}
