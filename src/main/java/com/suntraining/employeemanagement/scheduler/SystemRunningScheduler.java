package com.suntraining.employeemanagement.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class SystemRunningScheduler {

    private static final Logger log = LoggerFactory.getLogger(SystemRunningScheduler.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Task chạy định kỳ mỗi 30 giây (30000 ms) theo đúng yêu cầu đề bài
    @Scheduled(fixedRate = 30000)
    public void logSystemRunning() {
        log.info("System running - [Thời gian: {}]", LocalDateTime.now().format(FORMATTER));
    }
}
