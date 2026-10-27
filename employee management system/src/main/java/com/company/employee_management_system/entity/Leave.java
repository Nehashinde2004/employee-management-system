package com.company.employee_management_system.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "employee_leaves")
public class Leave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer leaveId;

    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
    private String status; // PENDING, APPROVED, REJECTED

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Leave() {
        super();
    }

    // Getters and Setters
    public Integer getLeaveId() { return leaveId; }
    public void setLeaveId(Integer leaveId) { this.leaveId = leaveId; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}