package com.company.employee_management_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee_management_system.dto.DepartmentDto;
import com.company.employee_management_system.entity.Department;
import com.company.employee_management_system.repository.copy.Departmentrepository;

@Service
@Transactional
public class Departmentservice {
	@Autowired
	Departmentrepository departmentrepository;

	public DepartmentDto savedepartment(DepartmentDto dto) {
		Department department = new Department();
		department.setDepartmentName(dto.getDepartmentName());
		Department savedepartment = departmentrepository.save(department);
		DepartmentDto departmentdto = new DepartmentDto();
		departmentdto.setDepartmentId(savedepartment.getDepartmentId());
		departmentdto.setDepartmentName(savedepartment.getDepartmentName());
		return departmentdto;

	}

	public DepartmentDto Findbyid(int id) {

		Department department = departmentrepository.findById(id)
				.orElseThrow(() -> new RuntimeException("department not found"));
		DepartmentDto dto = new DepartmentDto();
		dto.setDepartmentId(department.getDepartmentId());
		dto.setDepartmentName(department.getDepartmentName());
		return dto;

	}

	public List<DepartmentDto> findall() {
		List<Department> department = departmentrepository.findAll();
		List<DepartmentDto> dolist = new ArrayList<>();
		for (Department departments : department) {
			DepartmentDto dto = new DepartmentDto();
			dto.setDepartmentId(departments.getDepartmentId());
			dto.setDepartmentName(departments.getDepartmentName());
			dolist.add(dto);

		}
		return dolist;

	}

	public DepartmentDto update(DepartmentDto dto, int id) {

		Department department = departmentrepository.findById(id)
				.orElseThrow(() -> new RuntimeException("department not found"));
		department.setDepartmentName(dto.getDepartmentName());
		Department updatedep = departmentrepository.save(department);
		DepartmentDto departmentdto = new DepartmentDto();
		departmentdto.setDepartmentId(updatedep.getDepartmentId());
		departmentdto.setDepartmentName(updatedep.getDepartmentName());
		return departmentdto;
	}

	public void delete(int id) {

		Department department = departmentrepository.findById(id)
				.orElseThrow(() -> new RuntimeException("department not found"));
		departmentrepository.delete(department);

	}
}
