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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.company.employee_management_system.dto.ProjectDto;
import com.company.employee_management_system.service.ProjectService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    // SAVE PROJECT
    @PostMapping
    public ResponseEntity<ProjectDto> saveProject(
            @Valid @RequestBody ProjectDto dto) {

        ProjectDto project =
                projectService.saveProject(dto);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(project);
    }

    // FIND PROJECT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> findById(
            @PathVariable int id) {

        ProjectDto project =
                projectService.findById(id);

        return ResponseEntity.ok(project);
    }

    // FIND ALL PROJECTS
    @GetMapping
    public ResponseEntity<List<ProjectDto>> findAll() {

        List<ProjectDto> projects =
                projectService.findAll();

        return ResponseEntity.ok(projects);
    }

    // UPDATE PROJECT
    @PutMapping("/{id}")
    public ResponseEntity<ProjectDto> updateProject(
            @Valid @RequestBody ProjectDto dto,
            @PathVariable int id) {

        ProjectDto project =
                projectService.updateProject(dto, id);

        return ResponseEntity.ok(project);
    }

    // DELETE PROJECT
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(
            @PathVariable int id) {

        projectService.deleteProject(id);

        return ResponseEntity.ok(
                "Project deleted successfully"
        );
    }
}