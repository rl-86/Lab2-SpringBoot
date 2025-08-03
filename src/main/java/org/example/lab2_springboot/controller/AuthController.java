package org.example.lab2_springboot.controller;

import jakarta.validation.Valid;
import org.example.lab2_springboot.dto.LoginRequest;
import org.example.lab2_springboot.dto.RegisterRequest;
import org.example.lab2_springboot.dto.AuthResponse;
import org.example.lab2_springboot.model.Role;
import org.example.lab2_springboot.model.User;
import org.example.lab2_springboot.repository.UserRepository;
import org.example.lab2_springboot.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository,
                          PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            // Kontrollera om användaren redan finns
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Username already exists"));
            }

            // Skapa ny användare med korrekt rollhantering
            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

            // Sätt default roll till USER
            Set<Role> roles = new HashSet<>();
            roles.add(Role.USER); // Använd Role enum direkt
            user.setRoles(roles);

            // Spara användaren (endast en gång)
            User savedUser = userRepository.save(user);

            System.out.println("User registered: " + savedUser.getUsername() +
                    " with role: " + savedUser.getRoles());

            return ResponseEntity.ok(Map.of(
                    "message", "User registered successfully",
                    "username", savedUser.getUsername(),
                    "roles", savedUser.getRoles()
            ));

        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Autentisera användaren
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            // Hämta användarinformation
            User user = userRepository.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new BadCredentialsException("User not found"));

            // Generera JWT token
            List<Role> roles = new ArrayList<>(user.getRoles());
            String token = jwtUtil.generateToken(user.getUsername(), roles);

            // Returnera token och användarinfo
            AuthResponse response = new AuthResponse(
                    token,
                    user.getUsername(),
                    user.getRoles()
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid username or password"));
        } catch (Exception e) {
            System.err.println("Login error: " + e.getMessage());
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Login failed"));
        }
    }

    // Endpoint för att skapa admin (endast för testing/seeding)
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            if (userRepository.existsByUsername(registerRequest.getUsername())) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Username already exists"));
            }

            User user = new User();
            user.setUsername(registerRequest.getUsername());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

            // Sätt ADMIN roll
            Set<Role> roles = new HashSet<>();
            roles.add(Role.ADMIN);
            user.setRoles(roles);

            User savedUser = userRepository.save(user);

            return ResponseEntity.ok(Map.of(
                    "message", "Admin user registered successfully",
                    "username", savedUser.getUsername(),
                    "roles", savedUser.getRoles()
            ));

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Admin registration failed: " + e.getMessage()));
        }
    }
}