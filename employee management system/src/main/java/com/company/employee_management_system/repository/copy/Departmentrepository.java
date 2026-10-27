package com.company.employee_management_system.repository.copy;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.company.employee_management_system.entity.Department;

@Repository
public interface Departmentrepository  extends JpaRepository<Department, Integer> {
	
}
