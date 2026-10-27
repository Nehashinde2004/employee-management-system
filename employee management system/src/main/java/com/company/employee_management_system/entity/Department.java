package com.company.employee_management_system.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Department {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer departmentId;

	private String departmentName;
	@OneToMany(mappedBy = "department")
	private List<Employee> employee;

	public Department() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Department(Integer departmentId, String departmentName, List<Employee> employee) {
		super();
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.employee = employee;
	}

	public Integer getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Integer departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public List<Employee> getEmployee() {
		return employee;
	}

	public void setEmployee(List<Employee> employee) {
		this.employee = employee;
	}

}