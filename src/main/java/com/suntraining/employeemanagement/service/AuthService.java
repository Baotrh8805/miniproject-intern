package com.suntraining.employeemanagement.service;

import com.suntraining.employeemanagement.dto.AuthResponse;
import com.suntraining.employeemanagement.dto.LoginRequest;
import com.suntraining.employeemanagement.dto.RegisterRequest;
import com.suntraining.employeemanagement.model.Role;
import com.suntraining.employeemanagement.model.User;
import com.suntraining.employeemanagement.repository.UserRepository;
import com.suntraining.employeemanagement.security.JwtTokenProvider;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Tự động khởi tạo 2 tài khoản mẫu (ADMIN và USER) ban đầu nếu chưa tồn tại
    @PostConstruct
    public void initDefaultUsers() {
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(null, "admin", passwordEncoder.encode("admin123"), Role.ROLE_ADMIN);
            userRepository.save(admin);
            log.info("Khởi tạo tài khoản mẫu ADMIN thành công: username='admin', role='ROLE_ADMIN'");
        }

        if (!userRepository.existsByUsername("user")) {
            User normalUser = new User(null, "user", passwordEncoder.encode("user123"), Role.ROLE_USER);
            userRepository.save(normalUser);
            log.info("Khởi tạo tài khoản mẫu USER thành công: username='user', role='ROLE_USER'");
        }
    }

    // Đăng ký tài khoản người dùng mới
    public AuthResponse register(RegisterRequest request) {
        log.info("Yêu cầu đăng ký tài khoản mới: username='{}'", request.getUsername());

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Tên đăng nhập đã tồn tại: " + request.getUsername());
        }

        Role assignedRole = request.getRole() != null ? request.getRole() : Role.ROLE_USER;

        User user = new User();
        user.setUsername(request.getUsername().trim());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(assignedRole);

        User saved = userRepository.save(user);
        log.info("Đăng ký tài khoản thành công: ID={}, username='{}', role='{}'",
                saved.getId(), saved.getUsername(), saved.getRole());

        String token = jwtTokenProvider.generateToken(saved.getUsername(), saved.getRole().name());
        return new AuthResponse(token, saved.getUsername(), saved.getRole().name());
    }

    // Đăng nhập và xác thực, sinh JWT Token
    public AuthResponse login(LoginRequest request) {
        log.info("Yêu cầu đăng nhập: username='{}'", request.getUsername());

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Tài khoản hoặc mật khẩu không chính xác"));

        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
        log.info("Đăng nhập thành công: username='{}', role='{}'", user.getUsername(), user.getRole());

        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }
}
