package com.company.employee_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.company.employee_management_system.dto.Employeedto;
import com.company.employee_management_system.entity.Employee;
import com.company.employee_management_system.service.EmployeeService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;


    // SAVE EMPLOYEE
    @PostMapping
    public ResponseEntity<Employeedto> saveEmployee(
            @Valid @RequestBody Employeedto dto) {

        Employeedto employee = employeeService.saveEmployee(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(employee);
    }


    // FIND EMPLOYEE BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Employeedto> findById(
            @PathVariable int id) {

        Employeedto employee = employeeService.findById(id);

        return ResponseEntity.ok(employee);
    }


    // FIND ALL EMPLOYEES
    @GetMapping
    public ResponseEntity<List<Employeedto>> findAll() {

        List<Employeedto> employees =
                employeeService.findAll();

        return ResponseEntity.ok(employees);
    }


    // FIND EMPLOYEES BY DEPARTMENT
    @GetMapping("/department/{departmentName}")
    public ResponseEntity<List<Employeedto>> findByDepartment(
            @PathVariable String departmentName) {

        List<Employeedto> employees =
                employeeService.findByDepartmentDepartmentName(departmentName);

        return ResponseEntity.ok(employees);
    }


    // FIND EMPLOYEES BY PROJECT
    @GetMapping("/project/{projectName}")
    public ResponseEntity<List<Employeedto>> findByProject(
            @PathVariable String projectName) {

        List<Employeedto> employees =
                employeeService.findEmployeesByProject(projectName);

        return ResponseEntity.ok(employees);
    }


    // PAGINATION
    
    @GetMapping("/page")
    public ResponseEntity<Page<Employeedto>> findAllEmployees(
            Pageable pageable) {

        Page<Employeedto> employees =
                employeeService.findAllEmployees(pageable);

        return ResponseEntity.ok(employees);
    }


    // UPDATE EMPLOYEE
    
    @PutMapping("/{id}")
    public ResponseEntity<Employeedto> updateEmployee(
            @Valid @RequestBody Employeedto dto,
            @PathVariable int id) {

        Employeedto employee =
                employeeService.updateEmployee(dto, id);

        return ResponseEntity.ok(employee);
    }


    // DELETE EMPLOYEE
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable int id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok(
                "Employee deleted successfully");
    }
}