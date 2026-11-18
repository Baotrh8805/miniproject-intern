package com.suntraining.employeemanagement.dto;

import java.util.List;

public class CompanySummaryStatistic {

    private long totalEmployees;
    private long totalDepartments;
    private Double averageSalary;
    private Double maxSalary;
    private Double minSalary;
    private Double totalPayroll;
    private String generatedAt;
    private List<DepartmentStatistic> departmentStatistics;

    public CompanySummaryStatistic() {
    }

    public CompanySummaryStatistic(long totalEmployees, long totalDepartments, Double averageSalary,
                                   Double maxSalary, Double minSalary, Double totalPayroll,
                                   String generatedAt, List<DepartmentStatistic> departmentStatistics) {
        this.totalEmployees = totalEmployees;
        this.totalDepartments = totalDepartments;
        this.averageSalary = averageSalary != null ? Math.round(averageSalary * 100.0) / 100.0 : 0.0;
        this.maxSalary = maxSalary != null ? maxSalary : 0.0;
        this.minSalary = minSalary != null ? minSalary : 0.0;
        this.totalPayroll = totalPayroll != null ? Math.round(totalPayroll * 100.0) / 100.0 : 0.0;
        this.generatedAt = generatedAt;
        this.departmentStatistics = departmentStatistics;
    }

    public long getTotalEmployees() {
        return totalEmployees;
    }

    public void setTotalEmployees(long totalEmployees) {
        this.totalEmployees = totalEmployees;
    }

    public long getTotalDepartments() {
        return totalDepartments;
    }

    public void setTotalDepartments(long totalDepartments) {
        this.totalDepartments = totalDepartments;
    }

    public Double getAverageSalary() {
        return averageSalary;
    }

    public void setAverageSalary(Double averageSalary) {
        this.averageSalary = averageSalary;
    }

    public Double getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Double maxSalary) {
        this.maxSalary = maxSalary;
    }

    public Double getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Double minSalary) {
        this.minSalary = minSalary;
    }

    public Double getTotalPayroll() {
        return totalPayroll;
    }

    public void setTotalPayroll(Double totalPayroll) {
        this.totalPayroll = totalPayroll;
    }

    public String getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(String generatedAt) {
        this.generatedAt = generatedAt;
    }

    public List<DepartmentStatistic> getDepartmentStatistics() {
        return departmentStatistics;
    }

    public void setDepartmentStatistics(List<DepartmentStatistic> departmentStatistics) {
        this.departmentStatistics = departmentStatistics;
    }
}
