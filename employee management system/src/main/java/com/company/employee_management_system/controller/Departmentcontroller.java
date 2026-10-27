package com.company.employee_management_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.company.employee_management_system.dto.DepartmentDto;
import com.company.employee_management_system.service.Departmentservice;

@RestController
public class Departmentcontroller {
	@Autowired
	Departmentservice departmentservice;

	@PostMapping("/savedep")
	public ResponseEntity<DepartmentDto> savedepartment(@RequestBody DepartmentDto dto) {
		DepartmentDto dep = departmentservice.savedepartment(dto);
		return ResponseEntity.status(HttpStatus.CREATED).body(dep);

	}

	@GetMapping("/findbyid/{id}")
	public ResponseEntity<DepartmentDto> findbyid(@PathVariable int id) {
		DepartmentDto dto = departmentservice.Findbyid(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping("/findalldep")
	public ResponseEntity<List<DepartmentDto>> findall() {
		List<DepartmentDto> dto = departmentservice.findall();
		return new ResponseEntity<>(dto, HttpStatus.OK);

	}

	@PutMapping("/updatedep/{id}")
	public ResponseEntity<DepartmentDto> update(@RequestBody DepartmentDto dto, @PathVariable int id) {
		DepartmentDto updatedto = departmentservice.update(dto, id);
		return new ResponseEntity<>(updatedto, HttpStatus.OK);
	}

	@DeleteMapping("/deletedep/{id}")
	public ResponseEntity<String> deletedep(@PathVariable int id) {
		departmentservice.delete(id);
		return new ResponseEntity<>("department with this id is deletded", HttpStatus.OK);
	}
}
