package com.suntraining.employeemanagement.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UtilityService {

    private final Random random = new Random();

    // Chuẩn hóa chuỗi họ tên: bỏ khoảng trắng thừa, viết hoa chữ cái đầu
    public String formatString(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        String[] words = input.trim().split("\\s+");
        StringBuilder sb = new StringBuilder();

        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                  .append(word.substring(1).toLowerCase())
                  .append(" ");
            }
        }

        return sb.toString().trim();
    }

    // Tự động sinh mã nhân viên ngẫu nhiên dạng EMP-XXXX
    public String generateEmployeeCode() {
        int number = 1000 + random.nextInt(9000);
        return "EMP-" + number;
    }
}
