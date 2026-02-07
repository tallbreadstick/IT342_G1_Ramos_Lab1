package com.tallbreadstick.backend.service;

import com.tallbreadstick.backend.entity.BlacklistedToken;
import com.tallbreadstick.backend.entity.User;
import com.tallbreadstick.backend.repository.BlacklistedTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class TokenService {

    private final BlacklistedTokenRepository blacklistedTokenRepository;

    // Make sure this is at least 32 chars long
    private final String SECRET = "this_is_a_super_secret_key_123456!";
    private final long EXPIRATION_MS = 86400000; // 24h

    private final Key key;

    public TokenService(BlacklistedTokenRepository blacklistedTokenRepository) {
        this.blacklistedTokenRepository = blacklistedTokenRepository;
        this.key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    // Generate JWT
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validate JWT
    public boolean validateToken(String token) {
        if (blacklistedTokenRepository.existsById(token)) {
            return false;
        }

        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Extract user ID from JWT
    public Integer extractUserId(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return Integer.parseInt(claims.getSubject());
    }

    // Invalidate JWT by saving to blacklist
    public void invalidateToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        LocalDateTime expiry = claims.getExpiration()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        blacklistedTokenRepository.save(new BlacklistedToken(token, expiry));
    }
}
