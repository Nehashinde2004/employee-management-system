package com.company.employee_management_system.controller;

import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.employee_management_system.dto.EmployeePerformanceDto;
import com.company.employee_management_system.dto.TaskDto;
import com.company.employee_management_system.service.TaskService;
import com.example.ems.enums.TaskStatus;

@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // POST: Create / Assign a new task
    @PostMapping
    public ResponseEntity<TaskDto> createTask(
            @RequestBody TaskDto taskDTO) {

        TaskDto createdTask = taskService.createTask(taskDTO);

        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    // GET: Retrieve all tasks
    @GetMapping
    public ResponseEntity<List<TaskDto>> getAllTasks() {

        List<TaskDto> tasks = taskService.getAllTasks();

        return ResponseEntity.ok(tasks);
    }

    // GET: Retrieve task by ID
    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTaskById(
            @PathVariable Long id) {

        TaskDto task = taskService.getTaskById(id);

        return ResponseEntity.ok(task);
    }

    // GET: Retrieve tasks by Employee ID
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<TaskDto>> getTasksByEmployee(
            @PathVariable Integer employeeId) {

        List<TaskDto> tasks =
                taskService.getTasksByEmployee(employeeId);

        return ResponseEntity.ok(tasks);
    }

    // GET: Retrieve performance of all employees
    
    @GetMapping("/performance")
    public ResponseEntity<List<EmployeePerformanceDto>> getMonthlyEmployeePerformance(
            @RequestParam String month) {

        YearMonth selectedMonth = YearMonth.parse(month);

        return ResponseEntity.ok(
                taskService.getMonthlyEmployeePerformance(selectedMonth)
        );
    }
    

    // PUT: Update task status
    @PutMapping("/{id}/status")
    public ResponseEntity<TaskDto> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam TaskStatus status) {

        TaskDto updatedTask =
                taskService.updateTaskStatus(id, status);

        return ResponseEntity.ok(updatedTask);
    }

    // DELETE: Delete a task by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);

        return ResponseEntity.ok("Task deleted successfully!");
    }
}