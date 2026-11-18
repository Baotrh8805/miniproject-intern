package com.suntraining.employeemanagement.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF vì sử dụng kiến trúc Stateless API với JWT
                .csrf(AbstractHttpConfigurer::disable)

                // Cấu hình Session Stateless (không lưu session trên server)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Xử lý lỗi xác thực và phân quyền
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.getWriter().write("{\"status\":401,\"error\":\"Unauthorized\",\"message\":\"Bạn cần đăng nhập để truy cập tài nguyên này.\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setContentType("application/json;charset=UTF-8");
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.getWriter().write("{\"status\":403,\"error\":\"Forbidden\",\"message\":\"Bạn không có quyền thực hiện thao tác này (Chỉ dành cho ADMIN).\"}");
                        })
                )

                // Cấu hình phân quyền truy cập endpoint theo đúng yêu cầu đề bài
                .authorizeHttpRequests(auth -> auth
                        // 1. Cho phép công khai: Đăng ký, Đăng nhập, Actuator, Error
                        .requestMatchers("/api/auth/**", "/actuator/**", "/error").permitAll()

                        // 2. USER và ADMIN: Được quyền XEM danh sách và chi tiết nhân viên, phòng ban, thống kê báo cáo
                        .requestMatchers(HttpMethod.GET, "/employees/**", "/departments/**", "/api/statistics/**").hasAnyRole("USER", "ADMIN")

                        // 3. Chỉ ADMIN: Mới được quyền THÊM, SỬA, XÓA nhân viên (CRUD) và phòng ban
                        .requestMatchers(HttpMethod.POST, "/employees/**", "/departments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/employees/**", "/departments/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/employees/**", "/departments/**").hasRole("ADMIN")

                        // 4. Mọi yêu cầu còn lại đều cần đăng nhập
                        .anyRequest().authenticated()
                )

                // Hỗ trợ Basic Authentication để kiểm tra nhanh qua Postman / cURL
                .httpBasic(Customizer.withDefaults())

                // Thêm bộ lọc JWT trước bộ lọc UsernamePasswordAuthenticationFilter chuẩn
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
