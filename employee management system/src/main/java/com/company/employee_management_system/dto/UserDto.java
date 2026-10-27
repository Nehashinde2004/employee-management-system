package com.company.employee_management_system.dto;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {
	private Integer userId;
	@NotBlank(message = "Username is required")
	@Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
	private String username;
	@NotBlank(message = "Password is required")
	@Size(min = 8, max = 100, message = "Password must be at least 8 characters")
	private String password;
	@NotBlank(message = "Email is required")
	@Email(message = "Email must be valid")
	@Size(max = 254, message = "Email must not exceed 254 characters")
	private String email;
	private Integer employeeId;
	private Set<Integer> roleIds;

	// Getters and Setters
	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Set<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(Set<Integer> roleIds) {
		this.roleIds = roleIds;
	}
}