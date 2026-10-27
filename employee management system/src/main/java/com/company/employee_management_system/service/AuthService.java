
package com.company.employee_management_system.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(
            AuthenticationManager authenticationManager,
            JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public String login(String username, String password) {

        if (!StringUtils.hasText(username)
                || !StringUtils.hasText(password)) {
            throw new IllegalArgumentException(
                    "Username and password are required");
        }

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                username.trim(), password));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(
                    authentication.getName());
        }

        throw new AuthenticationException("Login failed") {
            private static final long serialVersionUID = 1L;
        };
    }
}
