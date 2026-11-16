package com.sun.training.employeemanagement.controller;

import com.sun.training.employeemanagement.model.Employee;
import com.sun.training.employeemanagement.service.EmployeeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // 1. API lấy danh sách nhân viên (hỗ trợ lọc theo phòng ban qua RequestParam)
    // Ví dụ: GET /employees hoặc GET /employees?department=IT
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(
            @RequestParam(required = false) String department) {
        List<Employee> list = employeeService.getAllEmployees(department);
        return ResponseEntity.ok(list);
    }

    // 2. API lấy chi tiết một nhân viên theo ID qua PathVariable
    // Ví dụ: GET /employees/1
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. API thêm mới nhân viên nhận dữ liệu từ RequestBody
    // Ví dụ: POST /employees với body JSON
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee savedEmployee = employeeService.addEmployee(employee);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }
}
