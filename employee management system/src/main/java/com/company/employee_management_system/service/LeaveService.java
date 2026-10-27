package com.company.employee_management_system.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee_management_system.dto.LeaveDto;
import com.company.employee_management_system.entity.Employee;
import com.company.employee_management_system.entity.Leave;
import com.company.employee_management_system.repository.copy.*;


@Service
@Transactional
public class LeaveService {

    @Autowired
    private LeaveRepository leaveRepository;

    @Autowired
    private Employeerepository employeeRepository;

    // APPLY FOR LEAVE
    public LeaveDto applyLeave(LeaveDto dto) {
        validateDates(dto.getStartDate(), dto.getEndDate());

        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + dto.getEmployeeId()));

        Leave leave = new Leave();
        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        // A leave applicant cannot approve their own request.
        leave.setStatus("PENDING");
        leave.setEmployee(employee); // Links the employee safely

        Leave savedLeave = leaveRepository.save(leave);
        return convertToDto(savedLeave);
    }

    // GET ALL LEAVES
    public List<LeaveDto> getAllLeaves() {
        return leaveRepository.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    // GET LEAVES BY EMPLOYEE ID
    public List<LeaveDto> getLeavesByEmployeeId(Integer employeeId) {
        return leaveRepository.findByEmployeeEmpid(employeeId).stream()
                .map(this::convertToDto)
                .toList();
    }

    // UPDATE LEAVE
    public LeaveDto updateLeave(Integer id, LeaveDto dto) {
        validateDates(dto.getStartDate(), dto.getEndDate());
        validateStatus(dto.getStatus());

        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave record not found with id: " + id));

        leave.setStartDate(dto.getStartDate());
        leave.setEndDate(dto.getEndDate());
        leave.setReason(dto.getReason());
        leave.setStatus(dto.getStatus());

        Leave updatedLeave = leaveRepository.save(leave);
        return convertToDto(updatedLeave);
    }

    // DELETE LEAVE
    @Transactional
    public void deleteLeave(Integer id) {
        Leave leave = leaveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave record not found with id: " + id));
        leaveRepository.delete(leave);
    }

    private void validateDates(java.time.LocalDate startDate, java.time.LocalDate endDate) {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required");
        }
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("End date cannot be before start date");
        }
    }

    private void validateStatus(String status) {
        if (status == null || !List.of("PENDING", "APPROVED", "REJECTED").contains(status)) {
            throw new IllegalArgumentException("Status must be PENDING, APPROVED, or REJECTED");
        }
    }

    // ENTITY TO DTO CONVERTER
    private LeaveDto convertToDto(Leave leave) {
        LeaveDto dto = new LeaveDto();
        dto.setLeaveId(leave.getLeaveId());
        dto.setStartDate(leave.getStartDate());
        dto.setEndDate(leave.getEndDate());
        dto.setReason(leave.getReason());
        dto.setStatus(leave.getStatus());
        if (leave.getEmployee() != null) {
            dto.setEmployeeId(leave.getEmployee().getEmpid());
        }
        return dto;
    }
}