
package com.company.employee_management_system.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee_management_system.dto.EmployeePerformanceDto;
import com.company.employee_management_system.dto.TaskDto;
import com.company.employee_management_system.entity.Employee;
import com.company.employee_management_system.entity.Project;
import com.company.employee_management_system.entity.Task;
import com.company.employee_management_system.repository.copy.Employeerepository;
import com.company.employee_management_system.repository.copy.ProjectRepository;
import com.company.employee_management_system.repository.copy.TaskRepository;
import com.example.ems.enums.TaskStatus;

@Service
@Transactional
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private Employeerepository employeeRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // Create / Assign Task
    public TaskDto createTask(TaskDto taskDTO) {

        Employee employee = employeeRepository.findById(taskDTO.getEmployeeId())
                .orElseThrow(() -> new RuntimeException(
                        "Employee not found with ID: " + taskDTO.getEmployeeId()));

        Project project = projectRepository.findById(taskDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException(
                        "Project not found with ID: " + taskDTO.getProjectId()));

        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus() != null
                ? taskDTO.getStatus()
                : TaskStatus.PENDING);
        task.setPriority(taskDTO.getPriority());
        task.setAssignedDate(taskDTO.getAssignedDate() != null
                ? taskDTO.getAssignedDate()
                : LocalDate.now());
        task.setDueDate(taskDTO.getDueDate());
        task.setEmployee(employee);
        task.setProject(project);

        Task savedTask = taskRepository.save(task);

        return mapToDTO(savedTask);
    }

    // Get All Tasks
    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get Task By ID
    public TaskDto getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Task not found with ID: " + id));

        return mapToDTO(task);
    }

    // Get Tasks By Employee ID
    public List<TaskDto> getTasksByEmployee(Integer employeeId) {

        return taskRepository.findByEmployee_Empid(employeeId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Update Task Status
    public TaskDto updateTaskStatus(Long id, TaskStatus status) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(
                        "Task not found with ID: " + id));

        task.setStatus(status);

        // Task entity handles completionDate automatically.
        Task updatedTask = taskRepository.save(task);

        return mapToDTO(updatedTask);
    }

    // Delete Task
    public void deleteTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Task not found with ID: " + id));
        taskRepository.delete(task);
    }

    // Monthly Performance of All Employees
    public List<EmployeePerformanceDto> getMonthlyEmployeePerformance(
            YearMonth month) {

        LocalDate startDate = month.atDay(1);
        LocalDate endDate = month.atEndOfMonth();

        List<Task> monthlyTasks =
                taskRepository.findByDueDateBetween(startDate, endDate);

        List<EmployeePerformanceDto> performanceList = new ArrayList<>();

        List<Employee> employees = employeeRepository.findAll();

        for (Employee employee : employees) {

            List<Task> employeeTasks = monthlyTasks.stream()
                    .filter(task -> task.getEmployee() != null
                            && task.getEmployee().getEmpid()
                                    .equals(employee.getEmpid()))
                    .collect(Collectors.toList());

            int totalTasks = employeeTasks.size();

            EmployeePerformanceDto dto = new EmployeePerformanceDto();
            dto.setEmployeeId(employee.getEmpid());
            dto.setEmployeeName(employee.getName());
            dto.setTotalTasks(totalTasks);

            // Employees without tasks should not receive a score of zero.
            if (totalTasks == 0) {
                dto.setCompletedTasks(0);
                dto.setCompletedOnTime(0);
                dto.setPerformanceScore(null);
                dto.setPerformanceLevel("No tasks assigned");

                performanceList.add(dto);
                continue;
            }

            int completedTasks = 0;
            int completedOnTime = 0;

            for (Task task : employeeTasks) {

                if (task.getStatus() == TaskStatus.COMPLETED) {

                    completedTasks++;

                    if (task.getCompletionDate() != null
                            && task.getDueDate() != null
                            && !task.getCompletionDate()
                                    .isAfter(task.getDueDate())) {
                        completedOnTime++;
                    }
                }
            }

            dto.setCompletedTasks(completedTasks);
            dto.setCompletedOnTime(completedOnTime);

            double completionRate =
                    (completedTasks * 100.0) / totalTasks;

            double onTimeRate = completedTasks == 0
                    ? 0.0
                    : (completedOnTime * 100.0) / completedTasks;

            // Weight: 70% completion and 30% on-time delivery.
            double score = (completionRate * 0.70)
                    + (onTimeRate * 0.30);

            score = Math.round(score * 100.0) / 100.0;

            dto.setPerformanceScore(score);

            if (score >= 90) {
                dto.setPerformanceLevel("Excellent");
            } else if (score >= 75) {
                dto.setPerformanceLevel("Good");
            } else if (score >= 50) {
                dto.setPerformanceLevel("Average");
            } else {
                dto.setPerformanceLevel("Needs Improvement");
            }

            performanceList.add(dto);
        }

        return performanceList;
    }

    // Helper method to convert Entity to DTO
    private TaskDto mapToDTO(Task task) {

        TaskDto dto = new TaskDto();

        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setStatus(task.getStatus());
        dto.setPriority(task.getPriority());
        dto.setAssignedDate(task.getAssignedDate());
        dto.setDueDate(task.getDueDate());
        dto.setCompletionDate(task.getCompletionDate());

        if (task.getProject() != null) {
            dto.setProjectId(task.getProject().getProjectId());
            dto.setProjectName(task.getProject().getProjectName());
        }

        if (task.getEmployee() != null) {
            dto.setEmployeeId(task.getEmployee().getEmpid());
            dto.setEmployeeName(task.getEmployee().getName());
        }

        return dto;
    }
}
