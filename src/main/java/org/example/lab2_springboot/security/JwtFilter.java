package org.example.lab2_springboot.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Tillåt auth endpoints utan JWT
        if (request.getServletPath().startsWith("/auth/")) {
            chain.doFilter(request, response);
            return;
        }

        String authorizationHeader = request.getHeader("Authorization");

        // Om ingen Authorization header finns, fortsätt (för publika endpoints)
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);

        try {
            String username = jwtUtil.extractUsername(token);

            // Validera token innan vi sätter authentication
            if (username != null && jwtUtil.validateToken(token, username)
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                List<String> roles = jwtUtil.extractRoles(token);

                // Skapa authorities - roller från JWT har redan ROLE_ prefix
                List<SimpleGrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new) // Använd rollen som den är
                        .collect(Collectors.toList());

                // Skapa authentication token
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

        } catch (Exception e) {
            // Logga fel men låt request fortsätta för publika endpoints
            System.err.println("JWT validation error: " + e.getMessage());
            // Rensa security context vid fel
            SecurityContextHolder.clearContext();
        }

        // Fortsätt alltid med filter chain
        chain.doFilter(request, response);
    }
}