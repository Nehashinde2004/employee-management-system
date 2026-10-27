package com.company.employee_management_system.repository.copy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.company.employee_management_system.entity.Employee;

@Repository
public interface Employeerepository extends JpaRepository<Employee, Integer> {

    List<Employee> findByDepartmentDepartmentName(String departmentName);

    @Query("""
           SELECT e
           FROM Employee e
           JOIN e.projects p
           WHERE p.projectName = :projectName
           """)
    List<Employee> findEmployeesByProject(
            @Param("projectName") String projectName);
}