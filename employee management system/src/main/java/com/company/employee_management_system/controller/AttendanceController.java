package com.company.employee_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.employee_management_system.dto.AttendanceDto;
import com.company.employee_management_system.service.AttendanceService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    // CREATE ATTENDANCE
    @PostMapping
    public ResponseEntity<AttendanceDto> saveAttendance(
            @Valid @RequestBody AttendanceDto dto) {

        AttendanceDto attendance =
                attendanceService.saveAttendance(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(attendance);
    }

    // GET ALL ATTENDANCE
    @GetMapping
    public ResponseEntity<List<AttendanceDto>> getAllAttendance() {

        List<AttendanceDto> attendance =
                attendanceService.getAllAttendance();

        return ResponseEntity.ok(attendance);
    }

    // GET ATTENDANCE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDto> getAttendanceById(
            @PathVariable Integer id) {

        AttendanceDto attendance =
                attendanceService.getAttendanceById(id);

        return ResponseEntity.ok(attendance);
    }

    // UPDATE ATTENDANCE
    @PutMapping("/{id}")
    public ResponseEntity<AttendanceDto> updateAttendance(
            @PathVariable Integer id,
            @Valid @RequestBody AttendanceDto dto) {

        AttendanceDto attendance =
                attendanceService.updateAttendance(id, dto);

        return ResponseEntity.ok(attendance);
    }

    // DELETE ATTENDANCE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAttendance(
            @PathVariable Integer id) {

        attendanceService.deleteAttendance(id);

        return ResponseEntity.ok(
                "Attendance deleted successfully");
    }
}