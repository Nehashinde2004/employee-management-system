package com.company.employee_management_system.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public class Employeedto {

    private Integer empid;

    @NotBlank(message = "Name is required")
    private String name;

    private Integer departmentId;

    private Integer addressId;

    private List<Integer> projectIds;

    public Employeedto() {
        super();
    }

    public Integer getEmpid() {
        return empid;
    }

    public void setEmpid(Integer empid) {
        this.empid = empid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getAddressId() {
        return addressId;
    }

    public void setAddressId(Integer addressId) {
        this.addressId = addressId;
    }

    public List<Integer> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<Integer> projectIds) {
        this.projectIds = projectIds;
    }
}