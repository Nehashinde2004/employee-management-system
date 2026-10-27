package com.company.employee_management_system.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.company.employee_management_system.dto.RoleDto;
import com.company.employee_management_system.service.RoleService;

import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {

	@Autowired
	private RoleService roleService;

	// CREATE ROLE
	@PostMapping
	public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto dto) {
		RoleDto createdRole = roleService.createRole(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
	}

	// GET ALL ROLES
	@GetMapping
	public ResponseEntity<List<RoleDto>> getAllRoles() {
		List<RoleDto> roles = roleService.getAllRoles();
		return ResponseEntity.ok(roles);
	}

	// GET ROLE BY ID
	@GetMapping("/{id}")
	public ResponseEntity<RoleDto> getRoleById(@PathVariable Integer id) {
		RoleDto role = roleService.getRoleById(id);
		return ResponseEntity.ok(role);
	}
}