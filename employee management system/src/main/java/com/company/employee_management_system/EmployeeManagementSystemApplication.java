package com.company.employee_management_system;

import com.company.employee_management_system.controller.TaskController;
import com.company.employee_management_system.repository.copy.TaskRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EmployeeManagementSystemApplication {

    private final TaskRepository taskRepository;

    private final TaskController taskController;

    EmployeeManagementSystemApplication(TaskController taskController, TaskRepository taskRepository) {
        this.taskController = taskController;
        this.taskRepository = taskRepository;
    }

	public static void main(String[] args) {

	    System.out.println("JWT_SECRET available: "
	        + (System.getenv("JWT_SECRET") != null
	        && !System.getenv("JWT_SECRET").isBlank()));

	    SpringApplication.run(
	        EmployeeManagementSystemApplication.class, args);
	    
	}
}
