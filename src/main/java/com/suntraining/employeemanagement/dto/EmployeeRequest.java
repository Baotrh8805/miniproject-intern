package com.suntraining.employeemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class EmployeeRequest {

    @NotBlank(message = "Tên nhân viên không được để trống")
    @Size(min = 2, max = 50, message = "Tên nhân viên phải từ 2 đến 50 ký tự")
    private String name;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotNull(message = "Mức lương không được để trống")
    @Min(value = 0, message = "Mức lương phải lớn hơn hoặc bằng 0")
    private Double salary;

    @NotNull(message = "Phòng ban không được để trống")
    private Long departmentId;

    public EmployeeRequest() {
    }

    public EmployeeRequest(String name, String email, Double salary, Long departmentId) {
        this.name = name;
        this.email = email;
        this.salary = salary;
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }
}
