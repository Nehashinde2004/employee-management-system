
package com.company.employee_management_system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.employee_management_system.dto.UserDto;
import com.company.employee_management_system.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 1. Register a new user
    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody UserDto dto) {

        UserDto createdUser = userService.registerUser(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    // 2. Get all users
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {

        List<UserDto> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    // 3. Get a user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @PathVariable Integer id) {

        UserDto user = userService.getUserById(id);

        return ResponseEntity.ok(user);
    }

    // 4. Assign an employee to a user
    @PutMapping("/{userId}/employee/{employeeId}")
    public ResponseEntity<UserDto> assignEmployee(
            @PathVariable Integer userId,
            @PathVariable Integer employeeId,
            Authentication authentication) {

        UserDto updatedUser = userService.assignEmployee(
                authentication.getName(),
                userId,
                employeeId
        );

        return ResponseEntity.ok(updatedUser);
    }
}
