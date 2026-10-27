
package com.company.employee_management_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee_management_system.dto.Employeedto;
import com.company.employee_management_system.entity.Address;
import com.company.employee_management_system.entity.Department;
import com.company.employee_management_system.entity.Employee;
import com.company.employee_management_system.entity.Project;
import com.company.employee_management_system.repository.copy.Addressrepository;
import com.company.employee_management_system.repository.copy.Departmentrepository;
import com.company.employee_management_system.repository.copy.Employeerepository;
import com.company.employee_management_system.repository.copy.ProjectRepository;

@Service
public class EmployeeService {

    @Autowired
    private Employeerepository employeeRepository;

    @Autowired
    private Departmentrepository departmentRepository;

    @Autowired
    private Addressrepository addressRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // SAVE EMPLOYEE
    @Transactional
    public Employeedto saveEmployee(Employeedto dto) {

        Employee employee = new Employee();
        employee.setName(dto.getName());

        // Department
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository
                    .findById(dto.getDepartmentId())
                    .orElseThrow(() ->
                            new RuntimeException("Department not found"));

            employee.setDepartment(department);
        }

        // Address
        if (dto.getAddressId() != null) {
            Address address = addressRepository
                    .findById(dto.getAddressId())
                    .orElseThrow(() ->
                            new RuntimeException("Address not found"));

            if (address.getEmployee() != null) {
                throw new RuntimeException(
                        "Address is already assigned to an employee");
            }

            // Employee is the owning side of this one-to-one relationship.
            // Do not point a managed Address at a transient Employee before save.
            employee.setAddress(address);
        }

        // Projects
        if (dto.getProjectIds() != null) {
            List<Project> projects =
                    projectRepository.findAllById(dto.getProjectIds());

            if (projects.size() != dto.getProjectIds().size()) {
                throw new RuntimeException(
                        "One or more projects were not found");
            }

            employee.setProjects(projects);
        }

        Employee savedEmployee = employeeRepository.save(employee);

        // Keep the inverse side consistent only after Employee has an ID.
        if (savedEmployee.getAddress() != null) {
            savedEmployee.getAddress().setEmployee(savedEmployee);
        }

        return convertToDto(savedEmployee);
    }

    // FIND EMPLOYEE BY ID
    @Transactional(readOnly = true)
    public Employeedto findById(int id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee with this id is not found"));

        return convertToDto(employee);
    }

    // FIND EMPLOYEES BY DEPARTMENT
    @Transactional(readOnly = true)
    public List<Employeedto> findByDepartmentDepartmentName(
            String departmentName) {

        List<Employee> employees =
                employeeRepository.findByDepartmentDepartmentName(
                        departmentName);

        return employees.stream()
                .map(this::convertToDto)
                .toList();
    }

    // FIND EMPLOYEES BY PROJECT
    @Transactional(readOnly = true)
    public List<Employeedto> findEmployeesByProject(String projectName) {

        List<Employee> employees =
                employeeRepository.findEmployeesByProject(projectName);

        return employees.stream()
                .map(this::convertToDto)
                .toList();
    }

    // FIND ALL EMPLOYEES
    @Transactional(readOnly = true)
    public List<Employeedto> findAll() {

        List<Employee> employees = employeeRepository.findAll();

        List<Employeedto> responseList = new ArrayList<>();

        for (Employee employee : employees) {
            responseList.add(convertToDto(employee));
        }

        return responseList;
    }

    // PAGINATION
    @Transactional(readOnly = true)
    public Page<Employeedto> findAllEmployees(Pageable pageable) {

        Page<Employee> employees = employeeRepository.findAll(pageable);

        return employees.map(this::convertToDto);
    }

    // UPDATE EMPLOYEE
    @Transactional
    public Employeedto updateEmployee(Employeedto dto, int id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee with this id is not found"));

        employee.setName(dto.getName());

        // Department
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository
                    .findById(dto.getDepartmentId())
                    .orElseThrow(() ->
                            new RuntimeException("Department not found"));

            employee.setDepartment(department);
        }

        // Address
        if (dto.getAddressId() != null) {

            Address newAddress = addressRepository
                    .findById(dto.getAddressId())
                    .orElseThrow(() ->
                            new RuntimeException("Address not found"));

            // Check whether this address belongs to another employee
            if (newAddress.getEmployee() != null
                    && !newAddress.getEmployee().getEmpid()
                            .equals(employee.getEmpid())) {

                throw new RuntimeException(
                        "Address is already assigned to another employee");
            }

            Address oldAddress = employee.getAddress();

            if (oldAddress != null
                    && !oldAddress.getAddressId()
                            .equals(newAddress.getAddressId())) {

                oldAddress.setEmployee(null);
            }

            employee.setAddress(newAddress);
            newAddress.setEmployee(employee);
        }

        // Projects
        if (dto.getProjectIds() != null) {
            List<Project> projects =
                    projectRepository.findAllById(dto.getProjectIds());

            if (projects.size() != dto.getProjectIds().size()) {
                throw new RuntimeException(
                        "One or more projects were not found");
            }

            employee.setProjects(projects);
        }

        Employee updatedEmployee = employeeRepository.save(employee);

        return convertToDto(updatedEmployee);
    }

    // DELETE EMPLOYEE
    @Transactional
    public void deleteEmployee(int id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee with this id is not found"));

        // Remove inverse address reference before deleting employee
        if (employee.getAddress() != null) {
            employee.getAddress().setEmployee(null);
        }

        employeeRepository.delete(employee);
    }

    // CONVERT ENTITY TO DTO
    private Employeedto convertToDto(Employee employee) {

        Employeedto dto = new Employeedto();

        dto.setEmpid(employee.getEmpid());
        dto.setName(employee.getName());

        if (employee.getDepartment() != null) {
            dto.setDepartmentId(
                    employee.getDepartment().getDepartmentId());
        }

        if (employee.getAddress() != null) {
            dto.setAddressId(
                    employee.getAddress().getAddressId());
        }

        if (employee.getProjects() != null) {
            List<Integer> projectIds = new ArrayList<>();

            for (Project project : employee.getProjects()) {
                projectIds.add(project.getProjectId());
            }

            dto.setProjectIds(projectIds);
        }

        return dto;
    }
}
