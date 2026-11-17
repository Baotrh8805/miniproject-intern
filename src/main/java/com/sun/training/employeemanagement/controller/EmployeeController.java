package com.sun.training.employeemanagement.controller;

import com.sun.training.employeemanagement.dto.EmployeeRequest;
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

    // 1. Lấy danh sách nhân viên & Tìm kiếm theo tên hoặc phòng ban
    // Ví dụ: GET /employees hoặc GET /employees?name=Nguyen hoặc GET /employees?departmentId=1
    @GetMapping
    public ResponseEntity<List<Employee>> getEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId) {
        return ResponseEntity.ok(employeeService.getAllEmployees(name, departmentId));
    }

    // 2. Lấy chi tiết một nhân viên theo ID
    // Ví dụ: GET /employees/1
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. Thêm mới nhân viên
    // Ví dụ: POST /employees kèm JSON EmployeeRequest trong body
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody EmployeeRequest request) {
        Employee created = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 4. Cập nhật thông tin nhân viên theo ID
    // Ví dụ: PUT /employees/1 kèm JSON cập nhật
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody EmployeeRequest request) {
        return employeeService.updateEmployee(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Xóa nhân viên theo ID
    // Ví dụ: DELETE /employees/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        boolean deleted = employeeService.deleteEmployee(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
