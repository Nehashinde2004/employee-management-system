package com.company.employee_management_system.dto;

import jakarta.validation.constraints.NotBlank;

public class DepartmentDto {

    private Integer departmentId;

    @NotBlank(message = "Department name is required")
    private String departmentName;

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}