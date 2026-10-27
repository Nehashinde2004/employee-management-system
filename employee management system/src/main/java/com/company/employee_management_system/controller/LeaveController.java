package com.company.employee_management_system.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.employee_management_system.dto.LeaveDto;
import com.company.employee_management_system.service.LeaveService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/leaves")
public class LeaveController {

    @Autowired
    private LeaveService leaveService;

    // APPLY FOR LEAVE
    @PostMapping
    public ResponseEntity<LeaveDto> applyLeave(@Valid @RequestBody LeaveDto dto) {
        LeaveDto leave = leaveService.applyLeave(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(leave);
    }

    // GET ALL LEAVES
    @GetMapping
    public ResponseEntity<List<LeaveDto>> getAllLeaves() {
        List<LeaveDto> leaves = leaveService.getAllLeaves();
        return ResponseEntity.ok(leaves);
    }

    // GET LEAVES BY EMPLOYEE ID
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<LeaveDto>> getLeavesByEmployeeId(@PathVariable Integer employeeId) {
        List<LeaveDto> leaves = leaveService.getLeavesByEmployeeId(employeeId);
        return ResponseEntity.ok(leaves);
    }

    // UPDATE LEAVE
    @PutMapping("/{id}")
    public ResponseEntity<LeaveDto> updateLeave(@PathVariable Integer id, @Valid @RequestBody LeaveDto dto) {
        LeaveDto leave = leaveService.updateLeave(id, dto);
        return ResponseEntity.ok(leave);
    }

    // DELETE LEAVE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLeave(@PathVariable Integer id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok("Leave request deleted successfully");
    }
}