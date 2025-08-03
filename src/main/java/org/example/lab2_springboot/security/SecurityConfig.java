package org.example.lab2_springboot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

@Configuration
public class SecurityConfig {
    private final JwtFilter jwtFilter;
    private final UserDetailsServiceImpl userDetailsService;

    public SecurityConfig(JwtFilter jwtFilter, UserDetailsServiceImpl userDetailsService) {
        this.jwtFilter = jwtFilter;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Auth endpoints - öppna för alla
                        .requestMatchers("/auth/**").permitAll()

                        // Categories - GET för alla, POST endast för ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/v1/categories/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/categories/**").hasRole("ADMIN")

                        // Places - mer granulär kontroll
                        .requestMatchers(HttpMethod.GET, "/api/v1/places/public/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/places/search/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/places/category/*/public").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/places/my/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/places/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/v1/places/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/places/**").authenticated()

                        // Fallback
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // ✅ Force 401
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Authentication required\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // ✅ 403 för fel behörigheter
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\": \"Access denied\"}");
                        })
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(authProvider));
    }
}