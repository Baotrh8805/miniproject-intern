package com.suntraining.employeemanagement.controller;

import com.suntraining.employeemanagement.service.UtilityService;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/demo")
public class DemoController {

    private final UtilityService utilityService;
    private final ModelMapper modelMapper;

    // Constructor Injection (DI)
    public DemoController(UtilityService utilityService, ModelMapper modelMapper) {
        this.utilityService = utilityService;
        this.modelMapper = modelMapper;
    }

    // Test chuẩn hóa chuỗi
    @GetMapping("/format-name")
    public ResponseEntity<Map<String, String>> formatName(
            @RequestParam(defaultValue = "  nguyen   van   a  ") String name) {
        Map<String, String> result = new HashMap<>();
        result.put("original", name);
        result.put("formatted", utilityService.formatString(name));
        return ResponseEntity.ok(result);
    }

    // Test sinh mã nhân viên tự động
    @GetMapping("/generate-code")
    public ResponseEntity<Map<String, String>> generateCode() {
        Map<String, String> result = new HashMap<>();
        result.put("employeeCode", utilityService.generateEmployeeCode());
        return ResponseEntity.ok(result);
    }

    // Test kiểm tra custom bean ModelMapper được inject thành công
    @GetMapping("/bean-check")
    public ResponseEntity<Map<String, Object>> checkBean() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "SUCCESS");
        result.put("message", "Custom Bean ModelMapper đã được inject thành công qua Constructor Injection");
        result.put("beanClass", modelMapper.getClass().getName());
        return ResponseEntity.ok(result);
    }
}
