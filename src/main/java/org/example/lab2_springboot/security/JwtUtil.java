package org.example.lab2_springboot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // 🛠️ Viktigt!
import java.util.Date;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890==";
    private static final long EXPIRATION_TIME = 86400000; // 24 timmar

    private SecretKey getSigningKey() { // 🛠️ Bytt från Key till SecretKey
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes); // 🛠️ Detta returnerar nu SecretKey
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser() // 🛠️ parser() istället för parserBuilder()
                .verifyWith(getSigningKey()) // 🛠️ Nu fungerar detta, eftersom det är SecretKey
                .build()
                .parseSignedClaims(token) // 🛠️ Uppdaterad metod
                .getPayload();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey()) // 🛠️ Nu fungerar det
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
