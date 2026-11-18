package com.suntraining.employeemanagement.service;

import com.suntraining.employeemanagement.dto.EmployeeRequest;
import com.suntraining.employeemanagement.exception.ResourceNotFoundException;
import com.suntraining.employeemanagement.model.Department;
import com.suntraining.employeemanagement.model.Employee;
import com.suntraining.employeemanagement.repository.DepartmentRepository;
import com.suntraining.employeemanagement.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService {

    private static final Logger log = LoggerFactory.getLogger(EmployeeService.class);

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
            log.info("CSDL chưa có dữ liệu nhân viên, đang nạp dữ liệu mẫu ban đầu...");
            Department itDept = departmentRepository.findByCode("IT").orElseGet(() ->
                    departmentRepository.save(new Department(null, "IT", "Phòng Công nghệ Thông tin", "Phát triển phần mềm"))
            );
            Department hrDept = departmentRepository.findByCode("HR").orElseGet(() ->
                    departmentRepository.save(new Department(null, "HR", "Phòng Nhân sự", "Quản lý tuyển dụng"))
            );

            employeeRepository.save(new Employee(null, "EMP-1001", "Nguyen Van A", "vana@sun.com", 1500.0, itDept));
            employeeRepository.save(new Employee(null, "EMP-1003", "Le Van C", "vanc@sun.com", 1800.0, itDept));
            employeeRepository.save(new Employee(null, "EMP-1002", "Tran Thi B", "thib@sun.com", 1200.0, hrDept));
            log.info("Nạp dữ liệu mẫu thành công: 3 nhân viên và 2 phòng ban.");
        }
    }

    // Lấy danh sách nhân viên kết hợp tìm kiếm theo tên và/hoặc phòng ban
    public List<Employee> getAllEmployees(String name, Long departmentId) {
        log.debug("Tìm kiếm danh sách nhân viên với bộ lọc: name='{}', departmentId={}", name, departmentId);

        boolean hasName = name != null && !name.trim().isEmpty();
        boolean hasDept = departmentId != null;

        List<Employee> results;
        if (hasName && hasDept) {
            results = employeeRepository.findByNameContainingIgnoreCaseAndDepartmentId(name.trim(), departmentId);
        } else if (hasName) {
            results = employeeRepository.findByNameContainingIgnoreCase(name.trim());
        } else if (hasDept) {
            results = employeeRepository.findByDepartmentId(departmentId);
        } else {
            results = employeeRepository.findAll();
        }

        log.debug("Tìm thấy {} nhân viên phù hợp.", results.size());
        return results;
    }

    // Lấy chi tiết nhân viên theo ID (ném ResourceNotFoundException nếu không tìm thấy)
    public Employee getEmployeeById(Long id) {
        log.debug("Lấy thông tin chi tiết nhân viên với ID: {}", id);
        return employeeRepository.findById(id).orElseThrow(() -> {
            log.warn("Không tìm thấy nhân viên với ID: {}", id);
            return new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id);
        });
    }

    // Thêm mới nhân viên vào CSDL
    public Employee createEmployee(EmployeeRequest request) {
        log.info("Bắt đầu thêm mới nhân viên: name='{}', email='{}', salary={}, departmentId={}",
                request.getName(), request.getEmail(), request.getSalary(), request.getDepartmentId());

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
                .orElseThrow(() -> {
                    log.error("Thêm nhân viên thất bại: Không tìm thấy phòng ban với ID: {}", request.getDepartmentId());
                    return new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + request.getDepartmentId());
                });
        employee.setDepartment(department);

        Employee saved = employeeRepository.save(employee);
        log.info("Thêm nhân viên thành công: ID={}, employeeCode='{}', name='{}', department='{}'",
                saved.getId(), saved.getEmployeeCode(), saved.getName(), saved.getDepartment().getName());
        return saved;
    }

    // Cập nhật thông tin nhân viên theo ID
    public Employee updateEmployee(Long id, EmployeeRequest request) {
        log.info("Bắt đầu cập nhật thông tin nhân viên ID: {}", id);

        Employee existing = employeeRepository.findById(id).orElseThrow(() -> {
            log.warn("Cập nhật thất bại: Không tìm thấy nhân viên với ID: {}", id);
            return new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id);
        });

        existing.setName(utilityService.formatString(request.getName()));
        existing.setEmail(request.getEmail());
        existing.setSalary(request.getSalary());

        Department department = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> {
                    log.error("Cập nhật thất bại: Không tìm thấy phòng ban với ID: {}", request.getDepartmentId());
                    return new ResourceNotFoundException("Không tìm thấy phòng ban với ID: " + request.getDepartmentId());
                });
        existing.setDepartment(department);

        Employee updated = employeeRepository.save(existing);
        log.info("Cập nhật thành công nhân viên ID: {}, employeeCode='{}', name='{}'",
                updated.getId(), updated.getEmployeeCode(), updated.getName());
        return updated;
    }

    // Xóa nhân viên theo ID
    public void deleteEmployee(Long id) {
        log.warn("Yêu cầu xóa nhân viên với ID: {}", id);

        Employee existing = employeeRepository.findById(id).orElseThrow(() -> {
            log.warn("Xóa thất bại: Không tìm thấy nhân viên với ID: {}", id);
            return new ResourceNotFoundException("Không tìm thấy nhân viên với ID: " + id);
        });

        employeeRepository.delete(existing);
        log.info("Đã xóa thành công nhân viên: ID={}, employeeCode='{}', name='{}'",
                existing.getId(), existing.getEmployeeCode(), existing.getName());
    }

    // Báo cáo tổng số nhân viên - Sử dụng Caching với thời gian sống TTL 1 phút (60 giây)
    @Cacheable(value = "employeeReport")
    public Map<String, Object> getEmployeeReport() {
        log.info("--> [CACHE MISS] Đang truy vấn CSDL để tính toán báo cáo tổng số nhân viên...");

        long totalEmployees = employeeRepository.count();
        long totalDepartments = departmentRepository.count();

        Map<String, Object> report = new HashMap<>();
        report.put("totalEmployees", totalEmployees);
        report.put("totalDepartments", totalDepartments);
        report.put("generatedAt", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        report.put("cachedFor", "1 phút (60 giây)");
        report.put("message", "Dữ liệu được lưu trong bộ nhớ đệm (Cache) trong vòng 1 phút kể từ thời điểm tạo.");

        return report;
    }
}
