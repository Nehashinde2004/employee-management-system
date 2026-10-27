package com.company.employee_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.company.employee_management_system.dto.RoleDto;
import com.company.employee_management_system.entity.Role;
import com.company.employee_management_system.repository.copy.RoleRepository;

import java.util.List;

@Service
@Transactional
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    // CREATE ROLE
    public RoleDto createRole(RoleDto dto) {
        Role role = new Role();
        role.setRoleName(dto.getRoleName());
        Role savedRole = roleRepository.save(role);
        return convertToDto(savedRole);
    }

    // GET ALL ROLES
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    // GET ROLE BY ID
    public RoleDto getRoleById(Integer id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return convertToDto(role);
    }

    // CONVERTER METHOD
    private RoleDto convertToDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setRoleId(role.getRoleId());
        dto.setRoleName(role.getRoleName());
        return dto;
    }
}