package com.company.employee_management_system.exception;

public class  EmployeeNotFoundException extends RuntimeException {

	public EmployeeNotFoundException(String msg) {
		super(msg);
	}
}
