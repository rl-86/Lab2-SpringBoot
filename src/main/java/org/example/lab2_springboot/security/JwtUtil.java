package org.example.lab2_springboot.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.lab2_springboot.model.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    // Läs från application.properties istället för hårdkodad
    @Value("${jwt.secret:myVerySecretKeyThatIsAtLeast256BitsLongForHmacSha256Algorithm}")
    private String secretKey;

    @Value("${jwt.expiration:86400000}") // 24 timmar default
    private long expirationTime;

    private SecretKey getSigningKey() {
        // Generera en säker nyckel från string
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(String username, List<Role> roles) {
        // Konvertera Role enum till strings för JWT
        List<String> roleStrings = roles.stream()
                .map(role -> role.getAuthority())
                .collect(Collectors.toList());

        return Jwts.builder()
                .subject(username)
                .claim("roles", roleStrings)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        return (List<String>) extractClaims(token).get("roles");
    }

    public Date extractExpiration(String token) {
        return extractClaims(token).getExpiration();
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (Exception e) {
            return true; // Om vi inte kan läsa token, behandla som expired
        }
    }

    public boolean validateToken(String token, String username) {
        try {
            return extractUsername(token).equals(username) && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}