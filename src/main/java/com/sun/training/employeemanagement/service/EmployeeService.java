package com.sun.training.employeemanagement.service;

import com.sun.training.employeemanagement.dto.EmployeeRequest;
import com.sun.training.employeemanagement.exception.ResourceNotFoundException;
import com.sun.training.employeemanagement.model.Department;
import com.sun.training.employeemanagement.model.Employee;
import com.sun.training.employeemanagement.repository.DepartmentRepository;
import com.sun.training.employeemanagement.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final UtilityService utilityService;

    public EmployeeService(EmployeeRepository employeeRepository,
                           DepartmentRepository departmentRepository,
                           UtilityService utilityService) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.utilityService = utilityService;
    }

    // Nạp sẵn nhân viên mẫu liên kết với phòng ban nếu CSDL chưa có dữ liệu
    @PostConstruct
    public void initDefaultEmployees() {
        if (employeeRepository.count() == 0) {
            Department itDept = departmentRepository.findByCode("IT").orElse(null);
            Department hrDept = departmentRepository.findByCode("HR").orElse(null);

            if (itDept != null) {
                employeeRepository.save(new Employee(null, "EMP-1001", "Nguyen Van A", "vana@sun.com", 1500.0, itDept));
                employeeRepository.save(new Employee(null, "EMP-1003", "Le Van C", "vanc@sun.com", 1800.0, itDept));
            }
            if (hrDept != null) {
                employeeRepository.save(new Employee(null, "EMP-1002", "Tran Thi B", "thib@sun.com", 1200.0, hrDept));
            }
        }
    }

    // Lấy danh sách nhân viên kết hợp tìm kiếm theo tên và/hoặc phòng ban
    public List<Employee> getAllEmployees(String name, Long departmentId) {
        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasDept = departmentId != null;

        if (hasName && hasDept) {
            return employeeRepository.findByNameContainingIgnoreCaseAndDepartmentId(name.trim(), departmentId);
        } else if (hasName) {
            return employeeRepository.findByNameContainingIgnoreCase(name.trim());
        } else if (hasDept) {
            return employeeRepository.findByDepartmentId(departmentId);
        }
        return employeeRepository.findAll();
    }

    // Lấy chi tiết nhân viên theo ID (ném ResourceNotFoundException nếu không tìm thấy)
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));
    }

    // Thêm mới nhân viên vào CSDL
    public Employee createEmployee(EmployeeRequest request) {
        Employee employee = new Employee();
        employee.setName(utilityService.formatString(request.getName()));
        employee.setEmail(request.getEmail());
        employee.setSalary(request.getSalary());

        // Tự động sinh mã nhân viên đảm bảo không trùng lặp
        String code;
        do {
            code = utilityService.generateEmployeeCode();
        } while (employeeRepository.existsByEmployeeCode(code));
        employee.setEmployeeCode(code);

        // Gán phòng ban
        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + request.getDepartmentId()));
        employee.setDepartment(department);

        return employeeRepository.save(employee);
    }

    // Cập nhật thông tin nhân viên theo ID
    public Employee updateEmployee(Long id, EmployeeRequest request) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));

        existing.setName(utilityService.formatString(request.getName()));
        existing.setEmail(request.getEmail());
        existing.setSalary(request.getSalary());

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + request.getDepartmentId()));
        existing.setDepartment(department);

        return employeeRepository.save(existing);
    }

    // Xóa nhân viên theo ID
    public void deleteEmployee(Long id) {
        Employee existing = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id));
        employeeRepository.delete(existing);
    }
}
