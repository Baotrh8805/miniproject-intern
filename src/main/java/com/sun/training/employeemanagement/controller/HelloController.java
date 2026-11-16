package com.sun.training.employeemanagement.controller;

import com.sun.training.employeemanagement.dto.HelloResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/hello")
public class HelloController {

    @GetMapping
    public ResponseEntity<HelloResponse> sayHello(@RequestParam(required = false) String name) {
        String greeting = (name != null && !name.trim().isEmpty())
                ? "Xin chào, " + name.trim() + "! Chào mừng bạn đến với Hệ thống Quản lý Nhân viên."
                : "Xin chào! Chào mừng bạn đến với Hệ thống Quản lý Nhân viên (Employee Management System).";

        HelloResponse response = HelloResponse.builder()
                .message(greeting)
                .status("UP")
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/text")
    public String sayHelloText() {
        return "Hello World! Project employee-management is running successfully.";
    }
}
