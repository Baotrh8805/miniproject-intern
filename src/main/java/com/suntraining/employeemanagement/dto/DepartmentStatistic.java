package com.suntraining.employeemanagement.dto;

public class DepartmentStatistic {

    private Long departmentId;
    private String departmentCode;
    private String departmentName;
    private Long employeeCount;
    private Double averageSalary;
    private Double totalSalary;
    private Double percentage; // Tỷ lệ % nhân viên so với toàn công ty

    public DepartmentStatistic() {
    }

    // Constructor dùng cho câu lệnh JPQL new com.suntraining.employeemanagement.dto.DepartmentStatistic(...)
    public DepartmentStatistic(Long departmentId, String departmentCode, String departmentName,
                               Long employeeCount, Double averageSalary, Double totalSalary) {
        this.departmentId = departmentId;
        this.departmentCode = departmentCode;
        this.departmentName = departmentName;
        this.employeeCount = employeeCount != null ? employeeCount : 0L;
        this.averageSalary = averageSalary != null ? Math.round(averageSalary * 100.0) / 100.0 : 0.0;
        this.totalSalary = totalSalary != null ? Math.round(totalSalary * 100.0) / 100.0 : 0.0;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(Long employeeCount) {
        this.employeeCount = employeeCount;
    }

    public Double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(Double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public Double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Double getPercentage() {
        return percentage;
    }

    public void setPercentage(Double percentage) {
        this.percentage = percentage;
    }
}
