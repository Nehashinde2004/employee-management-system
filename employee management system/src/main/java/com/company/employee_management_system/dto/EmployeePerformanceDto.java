
package com.company.employee_management_system.dto;

public class EmployeePerformanceDto {

	private Integer employeeId;
	private String employeeName;
	private int totalTasks;
	private int completedTasks;
	private int completedOnTime;
	private Double performanceScore;
	private String performanceLevel;

	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public int getTotalTasks() {
		return totalTasks;
	}

	public void setTotalTasks(int totalTasks) {
		this.totalTasks = totalTasks;
	}

	public int getCompletedTasks() {
		return completedTasks;
	}

	public void setCompletedTasks(int completedTasks) {
		this.completedTasks = completedTasks;
	}

	public int getCompletedOnTime() {
		return completedOnTime;
	}

	public void setCompletedOnTime(int completedOnTime) {
		this.completedOnTime = completedOnTime;
	}

	public Double getPerformanceScore() {
		return performanceScore;
	}

	public void setPerformanceScore(Double performanceScore) {
		this.performanceScore = performanceScore;
	}

	public String getPerformanceLevel() {
		return performanceLevel;
	}

	public void setPerformanceLevel(String performanceLevel) {
		this.performanceLevel = performanceLevel;
	}
}
