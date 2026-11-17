package com.suntraining.employeemanagement.controller;

import com.suntraining.employeemanagement.dto.EmployeeRequest;
import com.suntraining.employeemanagement.model.Employee;
import com.suntraining.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
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

    // 2. Lấy chi tiết một nhân viên theo ID (chỉ nhận số để tránh xung đột với các URL chữ như /list, /add)
    // Ví dụ: GET /employees/1
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    // 3. Thêm mới nhân viên kèm kiểm tra dữ liệu (@Valid)
    // Ví dụ: POST /employees kèm JSON body
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@Valid @RequestBody EmployeeRequest request) {
        Employee created = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // 4. Cập nhật thông tin nhân viên kèm kiểm tra dữ liệu (@Valid)
    // Ví dụ: PUT /employees/1 kèm JSON body
    @PutMapping("/{id:[0-9]+}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequest request) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, request));
    }

    // 5. Xóa nhân viên theo ID
    // Ví dụ: DELETE /employees/1
    @DeleteMapping("/{id:[0-9]+}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
