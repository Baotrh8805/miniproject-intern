package com.suntraining.employeemanagement.controller;

import com.suntraining.employeemanagement.dto.CompanySummaryStatistic;
import com.suntraining.employeemanagement.dto.DepartmentStatistic;
import com.suntraining.employeemanagement.service.StatisticService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticController {

    private static final Logger log = LoggerFactory.getLogger(StatisticController.class);

    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    // 1. API thống kê tổng quan toàn công ty (GET /api/statistics/summary)
    @GetMapping("/summary")
    public ResponseEntity<CompanySummaryStatistic> getCompanySummary() {
        log.info("[REST API] GET /api/statistics/summary - Yêu cầu lấy báo cáo thống kê tổng quan");
        return ResponseEntity.ok(statisticService.getCompanySummary());
    }

    // 2. API thống kê số lượng nhân viên theo từng phòng ban (GET /api/statistics/by-department)
    @GetMapping("/by-department")
    public ResponseEntity<List<DepartmentStatistic>> getDepartmentStatistics() {
        log.info("[REST API] GET /api/statistics/by-department - Yêu cầu lấy thống kê theo phòng ban");
        return ResponseEntity.ok(statisticService.getDepartmentStatistics());
    }
}
