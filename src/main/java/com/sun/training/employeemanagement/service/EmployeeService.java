package com.sun.training.employeemanagement.service;

import com.sun.training.employeemanagement.model.Employee;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    private final UtilityService utilityService;
    private final List<Employee> employees = new CopyOnWriteArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public EmployeeService(UtilityService utilityService) {
        this.utilityService = utilityService;

        // Khởi tạo sẵn một số dữ liệu nhân viên in-memory mẫu
        employees.add(new Employee(idCounter.getAndIncrement(), "EMP-1001", "Nguyen Van A", "vana@sun.com", "IT", 1500.0));
        employees.add(new Employee(idCounter.getAndIncrement(), "EMP-1002", "Tran Thi B", "thib@sun.com", "HR", 1200.0));
        employees.add(new Employee(idCounter.getAndIncrement(), "EMP-1003", "Le Van C", "vanc@sun.com", "IT", 1800.0));
    }

    // Lấy toàn bộ danh sách hoặc lọc theo phòng ban (nếu có tham số department)
    public List<Employee> getAllEmployees(String department) {
        if (department != null && !department.trim().isEmpty()) {
            return employees.stream()
                    .filter(e -> department.trim().equalsIgnoreCase(e.getDepartment()))
                    .collect(Collectors.toList());
        }
        return employees;
    }

    // Lấy chi tiết nhân viên theo ID
    public Optional<Employee> getEmployeeById(Long id) {
        return employees.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst();
    }

    // Thêm nhân viên mới vào danh sách in-memory
    public Employee addEmployee(Employee employee) {
        // Tái sử dụng UtilityService để chuẩn hóa họ tên
        employee.setName(utilityService.formatString(employee.getName()));

        // Tự động sinh mã nhân viên nếu chưa có
        if (employee.getEmployeeCode() == null || employee.getEmployeeCode().trim().isEmpty()) {
            employee.setEmployeeCode(utilityService.generateEmployeeCode());
        }

        // Tự động tăng ID
        employee.setId(idCounter.getAndIncrement());

        employees.add(employee);
        return employee;
    }
}
