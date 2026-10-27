package com.company.employee_management_system.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LeaveDto {

	private Integer leaveId;
	@NotNull(message = "Start date is required")
	private LocalDate startDate;
	@NotNull(message = "End date is required")
	private LocalDate endDate;
	@NotBlank(message = "Reason is required")
	@Size(max = 500, message = "Reason must not exceed 500 characters")
	private String reason;
	private String status;
	@NotNull(message = "Employee ID is required")
	private Integer employeeId;

	// Getters and Setters
	public Integer getLeaveId() {
		return leaveId;
	}

	public void setLeaveId(Integer leaveId) {
		this.leaveId = leaveId;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}
}