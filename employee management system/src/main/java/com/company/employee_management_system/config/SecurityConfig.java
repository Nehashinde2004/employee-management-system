
package com.company.employee_management_system.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.company.employee_management_system.service.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .cors(cors -> {})
            .csrf(csrf -> csrf.disable())

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authenticationProvider(authenticationProvider())

            .authorizeHttpRequests(auth -> auth

                // Allow CORS preflight requests
                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                // Allow root URL and error page
                .requestMatchers("/", "/error").permitAll()

                // Public registration and authentication endpoints
                .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                .requestMatchers("/api/auth/**").permitAll()

                // Admin-only endpoints
                .requestMatchers("/api/admin/**", "/roles/**")
                    .hasRole("ADMIN")

                // Employee write operations
                .requestMatchers(HttpMethod.POST, "/employees/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/employees/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/employees/**")
                    .hasRole("ADMIN")

                // Address write operations
                .requestMatchers(HttpMethod.POST, "/addresses/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/addresses/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/addresses/**")
                    .hasRole("ADMIN")

                // Project write operations
                .requestMatchers(HttpMethod.POST, "/projects/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/projects/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/projects/**")
                    .hasRole("ADMIN")

                // Department write operations
                .requestMatchers(HttpMethod.POST, "/savedep")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/updatedep/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/deletedep/**")
                    .hasRole("ADMIN")

                // Attendance write operations
                .requestMatchers(HttpMethod.POST, "/attendance/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/attendance/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/attendance/**")
                    .hasRole("ADMIN")

                // Leave write operations
                .requestMatchers(HttpMethod.POST, "/leaves/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/leaves/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/leaves/**")
                    .hasRole("ADMIN")

                // Task write operations
                .requestMatchers(HttpMethod.POST, "/api/tasks/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/tasks/**")
                    .hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/tasks/**")
                    .hasRole("ADMIN")

                // User management is admin-only
                .requestMatchers("/users/**").hasRole("ADMIN")

                // All other endpoints require authentication
                .anyRequest().authenticated()
            )

            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            )

            .httpBasic(basic -> basic.disable())
            .formLogin(form -> form.disable());

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "http://localhost:4200",
                "http://127.0.0.1:4200"
        ));

        configuration.setAllowedMethods(List.of(
                "GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"
        ));

        configuration.setAllowedHeaders(List.of(
                "Authorization", "Content-Type", "Accept"
        ));

        configuration.setExposedHeaders(List.of("Authorization"));
        configuration.setAllowCredentials(false);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }
}
