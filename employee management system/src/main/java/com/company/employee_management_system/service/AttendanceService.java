package com.company.employee_management_system.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee_management_system.dto.AttendanceDto;
import com.company.employee_management_system.entity.Attendance;
import com.company.employee_management_system.entity.Employee;
import com.company.employee_management_system.repository.copy.AttendanceRepository;
import com.company.employee_management_system.repository.copy.Employeerepository;

@Service
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private Employeerepository employeeRepository;

    // CREATE ATTENDANCE
    public AttendanceDto saveAttendance(AttendanceDto dto) {

        Employee employee = employeeRepository
                .findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        Attendance attendance = new Attendance();

        attendance.setAttendanceDate(dto.getAttendanceDate());
        attendance.setCheckIn(dto.getCheckIn());
        attendance.setCheckOut(dto.getCheckOut());
        attendance.setStatus(dto.getStatus());
        attendance.setEmployee(employee);

        Attendance savedAttendance =
                attendanceRepository.save(attendance);

        return convertToDto(savedAttendance);
    }

    // GET ALL ATTENDANCE
    public List<AttendanceDto> getAllAttendance() {

        return attendanceRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .toList();
    }

    // GET ATTENDANCE BY ID
    public AttendanceDto getAttendanceById(Integer id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Attendance not found"));

        return convertToDto(attendance);
    }

    // UPDATE ATTENDANCE
    public AttendanceDto updateAttendance(
            Integer id, AttendanceDto dto) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Attendance not found"));

        Employee employee = employeeRepository
                .findById(dto.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        attendance.setAttendanceDate(dto.getAttendanceDate());
        attendance.setCheckIn(dto.getCheckIn());
        attendance.setCheckOut(dto.getCheckOut());
        attendance.setStatus(dto.getStatus());
        attendance.setEmployee(employee);

        Attendance updatedAttendance =
                attendanceRepository.save(attendance);

        return convertToDto(updatedAttendance);
    }

    // DELETE ATTENDANCE
    @Transactional
    public void deleteAttendance(Integer id) {

        Attendance attendance = attendanceRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Attendance not found"));

        attendanceRepository.delete(attendance);
    }

    // ENTITY TO DTO
    private AttendanceDto convertToDto(Attendance attendance) {

        AttendanceDto dto = new AttendanceDto();

        dto.setAttendanceId(attendance.getAttendanceId());
        dto.setAttendanceDate(attendance.getAttendanceDate());
        dto.setCheckIn(attendance.getCheckIn());
        dto.setCheckOut(attendance.getCheckOut());
        dto.setStatus(attendance.getStatus());

        if (attendance.getEmployee() != null) {
            dto.setEmployeeId(
                    attendance.getEmployee().getEmpid());
        }

        return dto;
    }
}