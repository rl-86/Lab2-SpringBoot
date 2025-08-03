package org.example.lab2_springboot.dto;

import org.example.lab2_springboot.model.Role;
import java.util.Set;

public class AuthResponse {
    private String token;
    private String username;
    private Set<Role> roles;

    public AuthResponse(String token, String username, Set<Role> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }
}