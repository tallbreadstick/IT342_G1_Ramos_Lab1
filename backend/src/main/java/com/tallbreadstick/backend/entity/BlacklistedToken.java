package com.tallbreadstick.backend.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "blacklisted_tokens")
public class BlacklistedToken {

    @Id
    @Column(length = 255, nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiry;

    // ===== Constructors =====

    public BlacklistedToken() {}

    public BlacklistedToken(String token, LocalDateTime expiry) {
        this.token = token;
        this.expiry = expiry;
    }

    // ===== Getters and Setters =====

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public LocalDateTime getExpiry() {
        return expiry;
    }

    public void setExpiry(LocalDateTime expiry) {
        this.expiry = expiry;
    }
}
