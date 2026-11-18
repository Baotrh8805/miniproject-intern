package com.suntraining.employeemanagement.service;

import com.suntraining.employeemanagement.dto.CompanySummaryStatistic;
import com.suntraining.employeemanagement.dto.DepartmentStatistic;
import com.suntraining.employeemanagement.repository.DepartmentRepository;
import com.suntraining.employeemanagement.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StatisticService {

    private static final Logger log = LoggerFactory.getLogger(StatisticService.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;

    public StatisticService(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
    }

    // 1. Thống kê số lượng nhân viên và mức lương theo từng phòng ban
    public List<DepartmentStatistic> getDepartmentStatistics() {
        log.info("[STATISTICS] Tính toán thống kê nhân sự theo từng phòng ban...");

        List<DepartmentStatistic> stats = employeeRepository.getDepartmentStatistics();
        long totalEmployees = employeeRepository.countTotalEmployees();

        // Tính tỷ lệ % nhân viên của từng phòng ban so với toàn công ty
        for (DepartmentStatistic stat : stats) {
            if (totalEmployees > 0) {
                double pct = (stat.getEmployeeCount() * 100.0) / totalEmployees;
                stat.setPercentage(Math.round(pct * 10.0) / 10.0);
            } else {
                stat.setPercentage(0.0);
            }
        }

        return stats;
    }

    // 2. Thống kê tổng quan toàn hệ thống (Tổng NV, phòng ban, lương TB, quỹ lương...)
    public CompanySummaryStatistic getCompanySummary() {
        log.info("[STATISTICS] Tổng hợp báo cáo thống kê toàn hệ thống...");

        long totalEmployees = employeeRepository.countTotalEmployees();
        long totalDepartments = departmentRepository.count();
        Double averageSalary = employeeRepository.getAverageSalary();
        Double maxSalary = employeeRepository.getMaxSalary();
        Double minSalary = employeeRepository.getMinSalary();
        Double totalPayroll = employeeRepository.getTotalPayroll();
        String generatedAt = LocalDateTime.now().format(FORMATTER);

        List<DepartmentStatistic> departmentStats = getDepartmentStatistics();

        return new CompanySummaryStatistic(
                totalEmployees,
                totalDepartments,
                averageSalary,
                maxSalary,
                minSalary,
                totalPayroll,
                generatedAt,
                departmentStats
        );
    }
}
