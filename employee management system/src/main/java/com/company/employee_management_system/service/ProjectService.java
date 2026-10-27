package com.company.employee_management_system.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.employee_management_system.dto.ProjectDto;
import com.company.employee_management_system.entity.Project;
import com.company.employee_management_system.repository.copy.Departmentrepository;
import com.company.employee_management_system.repository.copy.ProjectRepository;

@Service
@Transactional
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	// SAVE PROJECT
	public ProjectDto saveProject(ProjectDto dto) {

		Project project = new Project();

		project.setProjectName(dto.getProjectName());
		project.setDescription(dto.getDescription());

		Project savedProject = projectRepository.save(project);

		ProjectDto response = new ProjectDto();

		response.setProjectId(savedProject.getProjectId());
		response.setProjectName(savedProject.getProjectName());
		response.setDescription(savedProject.getDescription());

		return response;
	}

	// FIND PROJECT BY ID
	public ProjectDto findById(int id) {

		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Project with this id is not found"));

		ProjectDto response = new ProjectDto();

		response.setProjectId(project.getProjectId());
		response.setProjectName(project.getProjectName());
		response.setDescription(project.getDescription());

		return response;
	}

	// FIND ALL PROJECTS
	public List<ProjectDto> findAll() {

		List<Project> projects = projectRepository.findAll();

		List<ProjectDto> responseList = new ArrayList<>();

		for (Project project : projects) {

			ProjectDto dto = new ProjectDto();

			dto.setProjectId(project.getProjectId());
			dto.setProjectName(project.getProjectName());
			dto.setDescription(project.getDescription());

			responseList.add(dto);
		}

		return responseList;
	}

	// UPDATE PROJECT
	public ProjectDto updateProject(ProjectDto dto, int id) {

		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Project with this id is not found"));

		project.setProjectName(dto.getProjectName());
		project.setDescription(dto.getDescription());

		Project updatedProject = projectRepository.save(project);

		ProjectDto response = new ProjectDto();

		response.setProjectId(updatedProject.getProjectId());

		response.setProjectName(updatedProject.getProjectName());

		response.setDescription(updatedProject.getDescription());

		return response;
	}

	// DELETE PROJECT
	public void deleteProject(int id) {

		Project project = projectRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Project with this id is not found"));

		projectRepository.delete(project);
	}
}