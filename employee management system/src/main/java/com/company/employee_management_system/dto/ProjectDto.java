package com.company.employee_management_system.dto;

import jakarta.validation.constraints.NotBlank;

public class ProjectDto {

    private Integer projectId;

    @NotBlank(message = "Project name is required")
    private String projectName;

    private String description;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}