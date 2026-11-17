package com.suntraining.employeemanagement.controller;

import com.suntraining.employeemanagement.dto.EmployeeRequest;
import com.suntraining.employeemanagement.model.Department;
import com.suntraining.employeemanagement.model.Employee;
import com.suntraining.employeemanagement.service.DepartmentService;
import com.suntraining.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

    private static final Logger log = LoggerFactory.getLogger(EmployeeWebController.class);

    private final EmployeeService employeeService;
    private final DepartmentService departmentService;

    public EmployeeWebController(EmployeeService employeeService, DepartmentService departmentService) {
        this.employeeService = employeeService;
        this.departmentService = departmentService;
    }

    // 1. Hiển thị danh sách nhân viên & Tìm kiếm theo tên/phòng ban
    // Hỗ trợ cả 2 endpoint: /employees/list và /employees/search
    @GetMapping({"/list", "/search"})
    public String listEmployees(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long departmentId,
            Model model) {

        log.info("Truy cập trang danh sách nhân viên [GET /employees/list] - Bộ lọc: name='{}', departmentId={}", name, departmentId);

        List<Employee> employees = employeeService.getAllEmployees(name, departmentId);
        List<Department> departments = departmentService.getAllDepartments();

        model.addAttribute("employees", employees);
        model.addAttribute("departments", departments);
        model.addAttribute("searchName", name != null ? name : "");
        model.addAttribute("searchDepartmentId", departmentId);

        return "employees/list";
    }

    // 2. Hiển thị form thêm mới nhân viên
    @GetMapping("/add")
    public String showAddForm(Model model) {
        log.info("Truy cập trang form thêm nhân viên mới [GET /employees/add]");
        model.addAttribute("employee", new EmployeeRequest());
        model.addAttribute("departments", departmentService.getAllDepartments());
        return "employees/add";
    }

    // 3. Xử lý lưu nhân viên mới có kiểm tra dữ liệu (@Valid + BindingResult)
    @PostMapping("/add")
    public String saveEmployee(
            @Valid @ModelAttribute("employee") EmployeeRequest request,
            BindingResult bindingResult,
            Model model) {

        log.info("Nhận dữ liệu submit thêm nhân viên [POST /employees/add]: name='{}', email='{}'",
                request.getName(), request.getEmail());

        if (bindingResult.hasErrors()) {
            log.warn("Dữ liệu form thêm nhân viên không hợp lệ ({} lỗi validation)", bindingResult.getErrorCount());
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employees/add";
        }

        Employee created = employeeService.createEmployee(request);
        log.info("Thêm nhân viên qua form thành công: mã={}, chuyển hướng về /employees/list", created.getEmployeeCode());
        return "redirect:/employees/list";
    }
}
