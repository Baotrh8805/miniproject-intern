package com.suntraining.employeemanagement.controller;

import com.suntraining.employeemanagement.dto.EmployeeRequest;
import com.suntraining.employeemanagement.model.Department;
import com.suntraining.employeemanagement.model.Employee;
import com.suntraining.employeemanagement.service.DepartmentService;
import com.suntraining.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/employees")
public class EmployeeWebController {

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

        if (bindingResult.hasErrors()) {
            // Khi có lỗi validation, nạp lại danh sách phòng ban để hiển thị dropdown và ở lại trang form
            model.addAttribute("departments", departmentService.getAllDepartments());
            return "employees/add";
        }

        employeeService.createEmployee(request);
        return "redirect:/employees/list";
    }
}
