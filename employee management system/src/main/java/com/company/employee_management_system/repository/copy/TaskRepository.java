
package com.company.employee_management_system.repository.copy;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.employee_management_system.entity.Task;
import com.example.ems.enums.TaskStatus;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

	// Find all tasks assigned to a specific employee
	List<Task> findByEmployee_Empid(Integer employeeId);

	// Find all tasks belonging to a specific project
	List<Task> findByProject_ProjectId(Integer projectId);

	// Find tasks by employee and status
	List<Task> findByEmployee_EmpidAndStatus(Integer employeeId, TaskStatus status);

	// Find tasks assigned to an employee within a date range
	List<Task> findByEmployee_EmpidAndAssignedDateBetween(Integer employeeId, LocalDate startDate, LocalDate endDate);

	// Find tasks completed by an employee within a date range
	List<Task> findByEmployee_EmpidAndCompletionDateBetween(Integer employeeId, LocalDate startDate, LocalDate endDate);

	List<Task> findByDueDateBetween(LocalDate startDate, LocalDate endDate);
}
