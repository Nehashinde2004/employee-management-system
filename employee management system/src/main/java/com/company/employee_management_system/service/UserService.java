
package com.company.employee_management_system.service;

import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee_management_system.dto.UserDto;
import com.company.employee_management_system.entity.Employee;
import com.company.employee_management_system.entity.Role;
import com.company.employee_management_system.entity.User;
import com.company.employee_management_system.repository.copy.Employeerepository;
import com.company.employee_management_system.repository.copy.RoleRepository;
import com.company.employee_management_system.repository.copy.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final Employeerepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            Employeerepository employeeRepository,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. Register a new user
    @Transactional
    public UserDto registerUser(UserDto dto) {

        String username = dto.getUsername().trim();
        String email = dto.getEmail().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException(
                    "Username is already registered");
        }

        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException(
                    "Email is already registered");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(email);

        // Public registration must never let a caller choose an ADMIN role
        // or claim an arbitrary employee record. Link employee accounts later
        // through the protected admin endpoint.
        Role employeeRole = roleRepository.findByRoleName("ROLE_EMPLOYEE")
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setRoleName("ROLE_EMPLOYEE");
                    return roleRepository.save(role);
                });

        user.setRoles(new HashSet<>(Set.of(employeeRole)));

        User savedUser = userRepository.save(user);

        return convertToDto(savedUser);
    }

    // 2. Get all users
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // 3. Get user by ID
    @Transactional(readOnly = true)
    public UserDto getUserById(Integer id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found with ID: " + id));

        return convertToDto(user);
    }

    // 4. Assign an employee to a user
    @Transactional
    public UserDto assignEmployee(
            String loggedInUsername,
            Integer targetUserId,
            Integer employeeId) {

        // Find logged-in user
        User loggedInUser = userRepository
                .findByUsername(loggedInUsername)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Logged-in user not found"));

        // Find target user
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "User not found with ID: " + targetUserId));

        // Check admin role
        boolean isAdmin = loggedInUser.getRoles() != null
                && loggedInUser.getRoles().stream()
                        .anyMatch(role ->
                                "ROLE_ADMIN".equals(role.getRoleName()));

        // Non-admin users can update only their own mapping
        if (!isAdmin
                && !loggedInUser.getUserId()
                        .equals(targetUser.getUserId())) {

            throw new AccessDeniedException(
                    "You can update only your own employee mapping");
        }

        // Find employee
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Employee not found with ID: " + employeeId));

        // Avoid assigning an employee already linked to another user
        if (userRepository.existsByEmployee_EmpidAndUserIdNot(
                employeeId, targetUserId)) {

            throw new IllegalArgumentException(
                    "This employee is already assigned to another user");
        }

        targetUser.setEmployee(employee);

        User updatedUser = userRepository.save(targetUser);

        return convertToDto(updatedUser);
    }

    // 5. Convert User entity to UserDto
    private UserDto convertToDto(User user) {

        UserDto dto = new UserDto();

        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        // Never return password hashes
        dto.setPassword(null);

        if (user.getEmployee() != null) {
            dto.setEmployeeId(user.getEmployee().getEmpid());
        }

        Set<Integer> roleIds = new HashSet<>();

        if (user.getRoles() != null) {
            for (Role role : user.getRoles()) {
                roleIds.add(role.getRoleId());
            }
        }

        dto.setRoleIds(roleIds);

        return dto;
    }
}
