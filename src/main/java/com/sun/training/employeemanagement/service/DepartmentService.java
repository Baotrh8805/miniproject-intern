package com.sun.training.employeemanagement.service;

import com.sun.training.employeemanagement.dto.DepartmentRequest;
import com.sun.training.employeemanagement.model.Department;
import com.sun.training.employeemanagement.repository.DepartmentRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    // Tự động tạo sẵn 2 phòng ban mẫu nếu bảng chưa có dữ liệu
    @PostConstruct
    public void initDefaultDepartments() {
        if (departmentRepository.count() == 0) {
            departmentRepository.save(new Department(null, "IT", "Phòng Công nghệ Thông tin", "Phát triển phần mềm"));
            departmentRepository.save(new Department(null, "HR", "Phòng Nhân sự", "Quản lý tuyển dụng"));
        }
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Optional<Department> getDepartmentById(Long id) {
        return departmentRepository.findById(id);
    }

    public Department createDepartment(DepartmentRequest request) {
        Department department = new Department();
        department.setCode(request.getCode().trim().toUpperCase());
        department.setName(request.getName().trim());
        department.setDescription(request.getDescription());
        return departmentRepository.save(department);
    }
}
